package com.shpp.eorlov.assignment1.ui.myContacts

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.shpp.eorlov.assignment1.R
import com.shpp.eorlov.assignment1.base.BaseFragment
import com.shpp.eorlov.assignment1.databinding.FragmentMyContactsBinding
import com.shpp.eorlov.assignment1.models.UserModel
import com.shpp.eorlov.assignment1.ui.SharedViewModel
import com.shpp.eorlov.assignment1.ui.contact_dialog_fragment.ContactDialogFragment
import com.shpp.eorlov.assignment1.ui.myContacts.adapter.MyContactsRecyclerAdapter
import com.shpp.eorlov.assignment1.ui.myContacts.adapter.listeners.ButtonClickListener
import com.shpp.eorlov.assignment1.ui.myContacts.adapter.listeners.ContactClickListener
import com.shpp.eorlov.assignment1.ui.viewpager.CollectionContactFragment
import com.shpp.eorlov.assignment1.ui.viewpager.CollectionContactFragmentDirections
import com.shpp.eorlov.assignment1.ui.viewpager.ContactCollectionAdapter
import com.shpp.eorlov.assignment1.utils.Constants.LIST_OF_CONTACTS_KEY
import com.shpp.eorlov.assignment1.utils.Constants.SNACKBAR_DURATION
import com.shpp.eorlov.assignment1.utils.Results
import com.shpp.eorlov.assignment1.utils.ext.clickWithDebounce
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyContactsFragment : BaseFragment(),
    ContactClickListener,
    ButtonClickListener {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: MyContactsViewModel by viewModels()

    private var swipeFlags = ItemTouchHelper.START

    private lateinit var binding: FragmentMyContactsBinding
    private lateinit var dialog: ContactDialogFragment

    private val contactsListAdapter: MyContactsRecyclerAdapter by lazy(LazyThreadSafetyMode.NONE) {
        MyContactsRecyclerAdapter(
            onContactClickListener = this
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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

    override fun onResume() {
        super.onResume()
        printLog("On resume")
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                (parentFragment as CollectionContactFragment).viewPager.currentItem =
                    ContactCollectionAdapter.ViewPagerItems.PROFILE.position
            }
        })
    }

    override fun onContactRemove(position: Int) {
        removeItemFromRecyclerView(position)
    }

    override fun onContactRemove(userModel: UserModel) {
        viewModel.removeItem(userModel)
    }

    override fun onContactSelected(contact: UserModel) {
        sharedElementTransitionWithSelectedContact(contact)
    }

    override fun onMultiselectActivated() {
        contactsListAdapter.selectAllContacts()
        binding.buttonRemoveSelectedContacts.visibility = View.VISIBLE
        binding.textViewAddContacts.visibility = View.GONE
        viewModel.selectedEvent.value = true
        disableContactSwipe()
        refreshRecyclerView()
    }

    override fun onContactSelectedStateChanged() {
        if (contactsListAdapter.areAllItemsUnselected()) {
            binding.buttonRemoveSelectedContacts.visibility = View.GONE
            viewModel.selectedEvent.value = false
            binding.textViewAddContacts.visibility = View.VISIBLE
            enableContactSwipe()
            refreshRecyclerView()
        }
    }

    override fun onGoUpClicked() {
        binding.recyclerViewMyContacts.smoothScrollToPosition(0)
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

    private fun disableContactSwipe() {
        swipeFlags = 0
    }

    private fun enableContactSwipe() {
        swipeFlags = ItemTouchHelper.START
    }


    private fun removeSelectedItemsFromRecyclerView() {
        viewModel.loadEvent.value = Results.LOADING
        contactsListAdapter.removeSelectedItems()
        refreshRecyclerView()
        if (viewModel.userListLiveData.value?.isEmpty() == true) {
            binding.buttonRemoveSelectedContacts.visibility = View.GONE
        }
        viewModel.loadEvent.value = Results.OK
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun refreshRecyclerView() {
//        contactsListAdapter.notifyDataSetChanged()
        binding.recyclerViewMyContacts.apply {
            adapter = contactsListAdapter
        }
    }

    private fun initRecycler() {
        /* Variable that implements swipe-to-delete */
        val itemTouchHelperCallBack: ItemTouchHelper.SimpleCallback =
            object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.START
            ) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    removeItemFromRecyclerView(
                        viewHolder.bindingAdapterPosition
                    )
                }

                override fun getMovementFlags(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder
                ): Int {

                    return makeMovementFlags(0, swipeFlags)
                }

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }
            }

        binding.recyclerViewMyContacts.apply {
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
        setViewModelObservers()
        setSharedViewModelObservers()
    }

    private fun setSharedViewModelObservers() {
        sharedViewModel.newUser.observe(viewLifecycleOwner) { newUser ->
            newUser?.let {
                viewModel.addItem(newUser)
                sharedViewModel.newUser.value = null
            }
        }

        sharedViewModel.getAllUsers.observe(viewLifecycleOwner) { usersList ->
            usersList?.let {
                viewModel.initializeData(usersList.data.users)
            }
        }
    }

    //fixme decomposition
    private fun setViewModelObservers() {
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
                            binding.contentLoadingProgressBar.isVisible = false
                        }

                        Results.LOADING -> {
                            lockUI()
                            binding.contentLoadingProgressBar.isVisible = true
                        }

                        Results.INITIALIZE_DATA_ERROR -> {
                            unlockUI()
                            binding.contentLoadingProgressBar.isVisible = false
                            Toast.makeText(requireContext(), event.name, Toast.LENGTH_LONG).show()
                        }
                        else -> {
                        }
                    }

                }
            }

            selectedEvent.apply {
                observe(viewLifecycleOwner) {
                    userListLiveData.value = userListLiveData.value
                }
            }
        }
    }


    private fun setListeners() {
        setOnClickListeners()
        setOnScrollListener()
    }

    private fun setOnScrollListener() {
        binding.apply {
            recyclerViewMyContacts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val userList = contactsListAdapter.currentList
                    if (userList.isEmpty()) return
                    if (!contactsListAdapter.isMultiSelect()) {
                        if (dy > 0 && buttonGoUp.visibility == View.VISIBLE) {
                            buttonGoUp.visibility = View.GONE
                        } else if (dy < 0 && buttonGoUp.visibility != View.VISIBLE) {
                            buttonGoUp.visibility = View.VISIBLE
                        }
                    }
                }
            })
        }
    }

    private fun setOnClickListeners() {
        binding.apply {

            textViewAddContacts.clickWithDebounce {
//                hideContacts()
//                hideMyContactsUI()
//                showAddContactsUI()
//                sharedViewModel.getAllUsers(viewModel.fetchToken())
            }

            buttonGoUp.setOnClickListener {
                recyclerViewMyContacts.smoothScrollToPosition(0)
                buttonGoUp.visibility = View.GONE
            }

            buttonRemoveSelectedContacts.clickWithDebounce {
                removeSelectedItemsFromRecyclerView()
                buttonRemoveSelectedContacts.visibility = View.GONE
            }

            imageButtonExit.clickWithDebounce {
                (parentFragment as CollectionContactFragment).viewPager.currentItem =
                    ContactCollectionAdapter.ViewPagerItems.PROFILE.position
            }
        }
    }
}