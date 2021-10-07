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
import com.shpp.eorlov.assignment1.databinding.ContactListItemBinding
import com.shpp.eorlov.assignment1.databinding.FragmentMyContactsBinding
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.ui.SharedViewModel
import com.shpp.eorlov.assignment1.ui.contactDialogFragment.ContactDialogFragment
import com.shpp.eorlov.assignment1.ui.myContacts.adapter.MyContactsRecyclerAdapter
import com.shpp.eorlov.assignment1.ui.myContacts.adapter.listeners.ButtonClickListener
import com.shpp.eorlov.assignment1.ui.myContacts.adapter.listeners.ContactClickListener
import com.shpp.eorlov.assignment1.ui.signIn.SignInFragmentDirections
import com.shpp.eorlov.assignment1.ui.viewPager.CollectionContactFragment
import com.shpp.eorlov.assignment1.ui.viewPager.CollectionContactFragmentDirections
import com.shpp.eorlov.assignment1.ui.viewPager.ContactCollectionAdapter
import com.shpp.eorlov.assignment1.utils.Constants
import com.shpp.eorlov.assignment1.utils.Constants.SNACKBAR_DURATION
import com.shpp.eorlov.assignment1.utils.Results
import com.shpp.eorlov.assignment1.utils.ext.clickWithDebounce
import dagger.hilt.android.AndroidEntryPoint


//todo fix bug with screen rotation
@AndroidEntryPoint
class MyContactsFragment : BaseFragment(),
    ContactClickListener,
    ButtonClickListener {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: MyContactsViewModel by viewModels()
    private val contactsListAdapter: MyContactsRecyclerAdapter by lazy(LazyThreadSafetyMode.NONE) {
        MyContactsRecyclerAdapter(
            onContactClickListener = this
        )
    }

    private var swipeFlags = ItemTouchHelper.START

    private lateinit var binding: FragmentMyContactsBinding
    private lateinit var itemBinding: ContactListItemBinding
    private lateinit var dialog: ContactDialogFragment //fixme remove it


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyContactsBinding.inflate(inflater, container, false)
        itemBinding = ContactListItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        setObservers()
        setListeners()
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

    override fun onContactClick(contact: UserModel) {
        sharedElementTransitionWithSelectedContact(contact)
    }

    override fun onMultiselectActivated() {
        contactsListAdapter.selectAllContacts()
        binding.buttonRemoveSelectedContacts.visibility = View.VISIBLE
        binding.textViewAddContacts.visibility = View.GONE
        viewModel.selectedEventLiveData.value = true
        disableContactSwipe()
        refreshRecyclerView()
    }

    override fun onContactSelectedStateChanged() {
        if (contactsListAdapter.areAllItemsUnselected()) {
            binding.buttonRemoveSelectedContacts.visibility = View.GONE
            viewModel.selectedEventLiveData.value = false
            binding.textViewAddContacts.visibility = View.VISIBLE
            enableContactSwipe()
            refreshRecyclerView()
        }
    }

    override fun onGoUpClicked() {
        binding.recyclerViewMyContacts.smoothScrollToPosition(0)
    }

    override fun onCheckedContactActivated() {
        binding.textViewContacts.text = getString(R.string.recommendation)
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
        viewModel.loadEventLiveData.value = Results.LOADING
        contactsListAdapter.removeSelectedItems()
        refreshRecyclerView()
        if (viewModel.contactsLiveData.value?.isEmpty() == true) {
            binding.buttonRemoveSelectedContacts.visibility = View.GONE
        }
        viewModel.loadEventLiveData.value = Results.OK
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun refreshRecyclerView() {
        contactsListAdapter.notifyDataSetChanged()
//     a   binding.recyclerViewMyContacts.apply {
//            adapter = contactsListAdapter
//        }
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
        sharedViewModel.newUserLiveData.observe(viewLifecycleOwner) { newUser ->
            newUser?.let {
                viewModel.addItem(newUser)
                sharedViewModel.newUserLiveData.value = null
            }
        }
    }

    //fixme decomposition
    private fun setViewModelObservers() {
        postponeEnterTransition()
        viewModel.apply {
            contactsLiveData.observe(viewLifecycleOwner) { list ->
                contactsListAdapter.submitList(list.toMutableList())
                loadEventLiveData.value = Results.OK //move to viewmodel

                // Start the transition once all views have been
                // measured and laid out
                (view?.parent as? ViewGroup)?.doOnPreDraw {
                    startPostponedEnterTransition()
                }
            }

            usersLiveData.observe(viewLifecycleOwner) {
                contactsListAdapter.submitList(it.toMutableList())
                loadEventLiveData.value = Results.OK //move to viewmodel
            }

            loadEventLiveData.apply {
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
                            Toast.makeText(requireContext(), event.name, Toast.LENGTH_LONG)
                                .show()
                        }
                        else -> {
                        }
                    }
                }
            }

            selectedEventLiveData.apply {
                observe(viewLifecycleOwner) {
                    contactsLiveData.value = contactsLiveData.value
                }
            }

            allUsersLiveData.observe(viewLifecycleOwner) {
                println("Work with ${it.data}")
                loadEventLiveData.value = Results.LOADING
                usersLiveData.value = it.data.users
            }
        }
    }


    private fun setListeners() {
        setOnClickListeners()
        setOnScrollListener()
    }

    private fun setOnScrollListener() {
        binding.apply {
            recyclerViewMyContacts.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
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
                viewModel.getAllUsers(viewModel.fetchToken())
                lockUI()
//                showProgressBar()

                showAddContactsUI()
                hideMyContactsUI()
//                refreshRecyclerView()
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
                if (binding.textViewContacts.text == getString(R.string.contacts)) {
                    (parentFragment as CollectionContactFragment).viewPager.currentItem =
                        ContactCollectionAdapter.ViewPagerItems.PROFILE.position
                } else {
                    viewModel.loadEventLiveData.value = Results.LOADING
                    viewModel.clearContactsList()
                    hideAddContactsUI()
                    showMyContactsUI()
                    viewModel.addItems(contactsListAdapter.getAddedItems())
                    refreshRecyclerView()
                }
            }
        }
    }

    private fun showMyContactsUI() {
        enableContactSwipe()
        binding.textViewContacts.text = getString(R.string.contacts)
        binding.textViewAddContacts.visibility = View.VISIBLE
        binding.buttonRemoveSelectedContacts.visibility = View.VISIBLE
    }

    private fun hideAddContactsUI() {
        contactsListAdapter.disableAddContactsState()
    }

    private fun showAddContactsUI() {
        viewModel.contactsLiveData.value?.let {
            contactsListAdapter.enableAddContactsState(it)
        }
    }

    private fun hideMyContactsUI() {
        disableContactSwipe()
        binding.textViewContacts.text = getString(R.string.users)
        binding.textViewAddContacts.visibility = View.GONE
        binding.buttonRemoveSelectedContacts.visibility = View.GONE
    }
}