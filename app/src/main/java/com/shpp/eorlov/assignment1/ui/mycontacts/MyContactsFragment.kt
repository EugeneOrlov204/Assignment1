package com.shpp.eorlov.assignment1.ui.mycontacts

import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.shpp.eorlov.assignment1.R
import com.shpp.eorlov.assignment1.databinding.FragmentMyContactsBinding
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.ui.MainActivity
import com.shpp.eorlov.assignment1.ui.SharedViewModel
import com.shpp.eorlov.assignment1.ui.dialogfragment.ContactDialogFragment
import com.shpp.eorlov.assignment1.ui.mycontacts.adapter.ContactClickListener
import com.shpp.eorlov.assignment1.ui.mycontacts.adapter.ContactsRecyclerAdapter
import com.shpp.eorlov.assignment1.ui.viewpager.CollectionContactFragmentDirections
import com.shpp.eorlov.assignment1.utils.Constants.BUTTON_CLICK_DELAY
import com.shpp.eorlov.assignment1.utils.Constants.CONTACT_DIALOG_TAG
import com.shpp.eorlov.assignment1.utils.Constants.LIST_OF_CONTACTS_KEY
import com.shpp.eorlov.assignment1.utils.Constants.SNACKBAR_DURATION
import com.shpp.eorlov.assignment1.utils.Results
import javax.inject.Inject
import kotlin.math.abs


class MyContactsFragment : Fragment(R.layout.fragment_my_contacts), ContactClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var viewModel: MyContactsViewModel

    private val contactsListAdapter: ContactsRecyclerAdapter by lazy {
        ContactsRecyclerAdapter(
            this
        )
    }

    private lateinit var binding: FragmentMyContactsBinding
    private lateinit var dialog: ContactDialogFragment


    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity as MainActivity).contactComponent.inject(this)

        viewModel =
            ViewModelProvider(this, viewModelFactory)[MyContactsViewModel::class.java]

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentMyContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        setObservers()
        setListeners()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val currentState = viewModel.userListLiveData.value ?: emptyList()
        outState.putParcelableArray(
            LIST_OF_CONTACTS_KEY,
            currentState.toTypedArray()
        )
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            val receivedState =
                savedInstanceState.getParcelableArray(LIST_OF_CONTACTS_KEY) ?: return

            val receivedList = mutableListOf<UserModel>()
            for (element in receivedState) {
                receivedList.add(element as UserModel)
            }

            viewModel.userListLiveData.value = receivedList
        }
    }

    override fun onContactRemove(position: Int) {
        removeItemFromRecyclerView(position)
    }

    override fun onContactSelected(contact: UserModel) {
        sharedElementTransitionWithSelectedContact(contact)
    }

    override fun onContactsSelected() {
        println("LONG SELECTED!")
    }


    /**
     * Removes item on given position from RecyclerView
     */
    fun removeItemFromRecyclerView(
        position: Int,
    ) {

        val removedItem: UserModel = viewModel.getItem(position) ?: return
        viewModel.removeItem(position)

        Snackbar.make(
            binding.root,
            getString(R.string.removed_contact_message),
            SNACKBAR_DURATION
        ).setAction("Cancel") {
            viewModel.addItem(position, removedItem)
        }.show()
    }

    private fun initRecycler() {
        viewModel.getPersonData()
        /* Variable that implements swipe-to-delete */
        val itemTouchHelperCallBack: ItemTouchHelper.SimpleCallback =
            object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.RIGHT
            ) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    removeItemFromRecyclerView(
                        viewHolder.bindingAdapterPosition
                    )
                }

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }
            }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = contactsListAdapter

            //Implement swipe-to-delete
            ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(this)
        }
    }

    private fun sharedElementTransitionWithSelectedContact(
        contact: UserModel,
    ) {

        val action =
            CollectionContactFragmentDirections.actionCollectionContactFragmentToDetailViewFragment(
                contact
            )
        findNavController().navigate(action)
    }


    private fun setObservers() {

        postponeEnterTransition()

        viewModel.apply {
            userListLiveData.observe(viewLifecycleOwner) { list ->
                contactsListAdapter.submitList(list.toMutableList())

                // Start the transition once all views have been
                // measured and laid out
                (view?.parent as? ViewGroup)?.doOnPreDraw {
                    startPostponedEnterTransition()
                }
            }

            loadEvent.apply {
                observe(viewLifecycleOwner) { event ->
                    when (event) {
                        Results.OK -> {
                            unlockUI()
                            binding.contentLoadingProgressBarRecyclerView.isVisible = false
                        }

                        Results.LOADING -> {
                            lockUI()
                            binding.contentLoadingProgressBarRecyclerView.isVisible = true
                        }

                        Results.INIT_RECYCLER_VIEW_ERROR -> {
                            unlockUI()
                            binding.contentLoadingProgressBarRecyclerView.isVisible = false
                            Toast.makeText(requireContext(), event.name, Toast.LENGTH_LONG).show()
                        }
                        else -> {
                        }
                    }

                }
            }
        }


        sharedViewModel.newUser.observe(viewLifecycleOwner) { newUser ->
            newUser?.let {
                viewModel.addItem(newUser)
                sharedViewModel.newUser.value = null
            }
        }
    }

    private fun lockUI() {
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun unlockUI() {
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private var previousClickTimestamp = SystemClock.uptimeMillis()


    private fun setListeners() {
        binding.textViewAddContacts.setOnClickListener {
            if (abs(SystemClock.uptimeMillis() - previousClickTimestamp) > BUTTON_CLICK_DELAY) {
                dialog = ContactDialogFragment()
                dialog.show(childFragmentManager, CONTACT_DIALOG_TAG)

                previousClickTimestamp = SystemClock.uptimeMillis()
            }
        }
    }
}