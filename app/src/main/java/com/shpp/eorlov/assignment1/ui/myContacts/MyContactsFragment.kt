package com.shpp.eorlov.assignment1.ui.myContacts

import android.R.attr
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
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.ui.SharedViewModel
import com.shpp.eorlov.assignment1.ui.myContacts.adapter.MyContactsRecyclerAdapter
import com.shpp.eorlov.assignment1.ui.myContacts.adapter.listeners.ContactClickListener
import com.shpp.eorlov.assignment1.ui.viewPager.CollectionContactFragment
import com.shpp.eorlov.assignment1.ui.viewPager.CollectionContactFragmentDirections
import com.shpp.eorlov.assignment1.ui.viewPager.ContactCollectionAdapter
import com.shpp.eorlov.assignment1.utils.Constants.SNACKBAR_DURATION
import com.shpp.eorlov.assignment1.utils.Results
import com.shpp.eorlov.assignment1.utils.ext.clickWithDebounce
import com.shpp.eorlov.assignment1.utils.ext.gone
import com.shpp.eorlov.assignment1.utils.ext.visible
import dagger.hilt.android.AndroidEntryPoint
import android.R.attr.data





@AndroidEntryPoint
class MyContactsFragment : BaseFragment(), ContactClickListener {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: MyContactsViewModel by viewModels()
    private val contactsListAdapter: MyContactsRecyclerAdapter by lazy(LazyThreadSafetyMode.NONE) {
        MyContactsRecyclerAdapter(
            onContactClickListener = this
        )
    }

    //Swiping direction for recycler view's items
    private var swipeFlags = ItemTouchHelper.START

    private lateinit var binding: FragmentMyContactsBinding


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
        viewModel.refreshContactsList()
        initRecycler()
        setObservers()
        setListeners()
    }

    override fun onResume() {
        super.onResume()
        printLog("On resume")
        goToMyProfile()
    }

    /**
     * Removes item from recycler view by clicking the remove button by position
     */
    override fun onContactRemove(position: Int) {
        removeItemFromRecyclerView(position)
    }

    /**
     * Removes item from recycler view by clicking the remove button by item
     */
    override fun onContactRemove(userModel: UserModel) {
        viewModel.removeItem(userModel)
    }

    /**
     * Goes to my detail fragment by clicking a contact
     */
    override fun onContactClick(contact: UserModel) {
        goToDetail(contact)
    }

    /**
     * Enables multi select state
     */
    override fun onMultiSelectEnabled() {
        contactsListAdapter.switchToMultiSelect()
        binding.buttonRemoveSelectedContacts.visible()
        binding.textViewAddContacts.gone()
        disableContactSwipe()
        refreshRecyclerView()
    }

    /**
     * Disables multi select if all recycler view's items is unselected
     */
    override fun onContactSelectedStateChanged() {
        if (contactsListAdapter.areAllItemsUnselected()) {
            disableMultiSelect()
        }
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

    /**
     * Disables multi select state
     */
    private fun disableMultiSelect() {
        binding.buttonRemoveSelectedContacts.visible()
        binding.textViewAddContacts.visible()
        enableContactSwipe()
        refreshRecyclerView()
    }

    /**
     * Goes back to my profile fragment by clicking the back button
     */
    private fun goToMyProfile() {
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                (parentFragment as CollectionContactFragment).viewPager.currentItem =
                    ContactCollectionAdapter.ViewPagerItems.PROFILE.position
            }
        })
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
            binding.buttonRemoveSelectedContacts.gone()
        }
        viewModel.ok()
    }

    private fun refreshRecyclerView() {
       binding.recyclerViewMyContacts.adapter = contactsListAdapter
    }

    private fun initRecycler() {
        /* Variable that implements swipe-to-delete */
        val itemTouchHelperCallBack: ItemTouchHelper.SimpleCallback =
            object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.START
            ) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    removeItemFromRecyclerView(viewHolder.bindingAdapterPosition)
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


    private fun goToDetail(contact: UserModel) {
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
        sharedViewModel.apply {
            newUserLiveData.observe(viewLifecycleOwner) { newUser ->
                newUser?.let {
                    viewModel.addItem(newUser)
                    sharedViewModel.newUserLiveData.value = null
                }
            }
        }
    }

    private fun setViewModelObservers() {
        postponeEnterTransition()
        viewModel.apply {
            contactsLiveData.observe(viewLifecycleOwner) { list ->
                contactsListAdapter.submitList(list.toMutableList())
                viewModel.ok()

                // Start the transition once all views have been
                // measured and laid out
                (view?.parent as? ViewGroup)?.doOnPreDraw {
                    startPostponedEnterTransition()
                }
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
                            buttonGoUp.gone()
                        } else if (dy < 0 && buttonGoUp.visibility != View.VISIBLE) {
                            buttonGoUp.visible()
                        }
                    }
                }
            })
        }
    }

    private fun setOnClickListeners() {
        binding.apply {

            textViewAddContacts.clickWithDebounce {
                val action =
                    CollectionContactFragmentDirections.actionCollectionContactFragmentToAddContactsFragment(
                        viewModel.contactsLiveData.value?.toTypedArray() ?: return@clickWithDebounce
                    )
                findNavController().navigate(action)
            }

            buttonGoUp.setOnClickListener {
                recyclerViewMyContacts.smoothScrollToPosition(0)
                buttonGoUp.gone()
            }

            buttonRemoveSelectedContacts.clickWithDebounce {
                removeSelectedItemsFromRecyclerView()
                buttonRemoveSelectedContacts.gone()
            }

            imageButtonExit.clickWithDebounce {
                (parentFragment as CollectionContactFragment).viewPager.currentItem =
                    ContactCollectionAdapter.ViewPagerItems.PROFILE.position
            }
        }
    }
}