package com.shpp.eorlov.assignment1.ui.addContacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.shpp.eorlov.assignment1.R
import com.shpp.eorlov.assignment1.base.BaseFragment
import com.shpp.eorlov.assignment1.databinding.FragmentAddContactsBinding
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.model.Users
import com.shpp.eorlov.assignment1.ui.addContacts.adapter.AddContactsListAdapter
import com.shpp.eorlov.assignment1.ui.addContacts.adapter.listeners.IAddContactClickListener
import com.shpp.eorlov.assignment1.ui.contactProfile.ContactProfileFragment
import com.shpp.eorlov.assignment1.utils.FeatureNavigationEnabled
import com.shpp.eorlov.assignment1.utils.TransitionKeys.ADDED_CONTACTS_KEY
import com.shpp.eorlov.assignment1.utils.TransitionKeys.USERS_KEY
import com.shpp.eorlov.assignment1.utils.TransitionKeys.USER_MODEL_KEY
import com.shpp.eorlov.assignment1.utils.ext.clickWithDebounce
import com.shpp.eorlov.assignment1.utils.ext.gone
import com.shpp.eorlov.assignment1.utils.ext.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddContactsFragment : BaseFragment(), IAddContactClickListener {
    private val viewModel: AddContactsViewModel by viewModels()
    private val args: AddContactsFragmentArgs by navArgs()
    private val selectedItems = Users()
    private val contactsListAdapter: AddContactsListAdapter by lazy(LazyThreadSafetyMode.NONE) {
        AddContactsListAdapter(
            contactClickListener = this,
            selectedItems
        )
    }

    //True if user find some contacts using search field, otherwise false
    private var foundContacts = false

    private lateinit var binding: FragmentAddContactsBinding
    private lateinit var addedContacts: Array<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addedContacts = if (FeatureNavigationEnabled.featureNavigationEnabled) {
            args.addedContacts
        } else {
            requireArguments().getParcelableArray(ADDED_CONTACTS_KEY) ?: return
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initRecycler()
        setObservers()
        setListeners()
    }

    override fun onResume() {
        super.onResume()
        printLog("On resume")
    }

    override fun onAddContact(contact: UserModel) {
        if (binding.textViewTitle.text != getString(R.string.recommendation)) {
            binding.textViewTitle.text = getString(R.string.recommendation)
        }

        if (FeatureNavigationEnabled.featureNavigationEnabled) {
            val action =
                AddContactsFragmentDirections.actionAddContactsFragmentToContactProfileFragment(
                    contact,
                    selectedItems
                )
            findNavController().navigate(action)
        } else {
            val fragmentManager = activity?.supportFragmentManager

            val arguments = Bundle().apply {
                putParcelable(USER_MODEL_KEY, contact)
                putParcelable(USERS_KEY, selectedItems)
            }

            fragmentManager?.commit {
                setReorderingAllowed(true)
                replace(R.id.fragmentContainerView, ContactProfileFragment().apply {
                    this.arguments = arguments
                })
                addToBackStack(null)
            }
        }
    }

    private fun initViews() {
        if (addedContacts.isNotEmpty()) {
            binding.textViewTitle.text = getString(R.string.recommendation)
        }
    }

    private fun initRecycler() {
        addedContacts.map { selectedItems.add(it as UserModel) }

        binding.recyclerViewAddContacts.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = contactsListAdapter
        }
    }

    private fun setListeners() {
        setOnClickListeners()
        setOnEditorActionListeners()
    }

    private fun setOnClickListeners() {
        binding.apply {
            imageButtonExit.clickWithDebounce {
                lockUI()
                viewModel.addItems(contactsListAdapter.getAddedItems())
                viewModel.loading()
                binding.contentLoadingProgressBar.isVisible = true
            }

            buttonGoUp.setOnClickListener {
                recyclerViewAddContacts.smoothScrollToPosition(0)
                buttonGoUp.visible()
            }

            imageButtonSearchButton.clickWithDebounce {
                hideUsersTitleUI()
                showSearchFieldUI()
            }

            imageButtonCancelButton.clickWithDebounce {
                hideSearchFieldUI()
                showUsersTitleUI()
                if (!foundContacts) {
                    showRecyclerViewUI()
                    hideNoResultsFoundUI()
                }
                viewModel.clearSearchedContacts()
            }

            textInputLayoutSearchContacts.setEndIconOnClickListener {
                searchContacts()
            }
        }
    }

    private fun setOnEditorActionListeners() {
        binding.textInputEditTextSearchContacts.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchContacts()
            }
            false
        }
    }

    private fun setObservers() {
        viewModel.apply {
            usersLiveData.observe(viewLifecycleOwner) {
                contactsListAdapter.submitList(it.toMutableList())
                clearSearchField()
            }

            searchedContactsLiveData.observe(viewLifecycleOwner) { list ->
                if (list.isNotEmpty()) {
                    contactsListAdapter.submitList(list.toMutableList())
                }
                viewModel.ok()
            }

            isLoadedListLiveData.observe(viewLifecycleOwner) { isLoaded ->
                if (isLoaded) {
                    binding.contentLoadingProgressBar.isVisible = false
                    unlockUI()
                    activity?.onBackPressed()
                }
            }
        }

    }

    private fun searchContacts() {
        foundContacts =
            viewModel.searchContacts(binding.textInputEditTextSearchContacts.text.toString())

        apply {
            if (!foundContacts) {
                hideRecyclerViewUI()
                showNoResultsFoundUI()
            } else {
                showRecyclerViewUI()
                hideNoResultsFoundUI()
            }
        }
    }

    private fun hideNoResultsFoundUI() {
        binding.textViewNoResultsFound.gone()
        binding.textViewMoreContactsInRecommendation.gone()
    }

    private fun showNoResultsFoundUI() {
        binding.textViewNoResultsFound.visible()
        binding.textViewMoreContactsInRecommendation.visible()
    }

    private fun showRecyclerViewUI() {
        binding.recyclerViewAddContacts.visible()
    }

    private fun hideRecyclerViewUI() {
        binding.recyclerViewAddContacts.gone()
    }

    private fun hideSearchFieldUI() {
        binding.apply {
            textInputLayoutSearchContacts.gone()
            imageButtonCancelButton.gone()
        }
    }

    private fun clearSearchField() {
        binding.textInputEditTextSearchContacts.setText("")
    }

    private fun showSearchFieldUI() {
        binding.apply {
            textInputLayoutSearchContacts.visible()
            imageButtonCancelButton.visible()
        }
    }

    private fun showUsersTitleUI() {
        binding.apply {
            textViewTitle.visible()
            imageButtonExit.visible()
            imageButtonSearchButton.visible()
        }
    }

    private fun hideUsersTitleUI() {
        binding.apply {
            textViewTitle.gone()
            imageButtonExit.gone()
            imageButtonSearchButton.gone()
        }
    }
}