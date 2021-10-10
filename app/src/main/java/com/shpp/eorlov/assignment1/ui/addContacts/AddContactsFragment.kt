package com.shpp.eorlov.assignment1.ui.addContacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.shpp.eorlov.assignment1.R
import com.shpp.eorlov.assignment1.base.BaseFragment
import com.shpp.eorlov.assignment1.databinding.FragmentAddContactsBinding
import com.shpp.eorlov.assignment1.ui.addContacts.adapter.AddContactsRecyclerAdapter
import com.shpp.eorlov.assignment1.ui.addContacts.adapter.listeners.IAddContactClickListener
import com.shpp.eorlov.assignment1.utils.ext.clickWithDebounce
import com.shpp.eorlov.assignment1.utils.ext.gone
import com.shpp.eorlov.assignment1.utils.ext.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddContactsFragment : BaseFragment(), IAddContactClickListener {
    private val viewModel: AddContactsViewModel by viewModels()
    private val args: AddContactsFragmentArgs by navArgs()
    private val contactsListAdapter: AddContactsRecyclerAdapter by lazy(LazyThreadSafetyMode.NONE) {
        AddContactsRecyclerAdapter(
            contactClickListener = this,
            arrayListOf(*args.addedContacts)
        )
    }

    //True if user find some contacts using search field, otherwise false
    private var foundContacts = false

    private lateinit var binding: FragmentAddContactsBinding

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
        if (args.addedContacts.isNotEmpty()) onCheckedContacts()
        initRecycler()
        setObservers()
        setListeners()
    }

    override fun onResume() {
        super.onResume()
        printLog("On resume")
    }

    override fun onCheckedContacts() {
        binding.textViewTitle.text = getString(R.string.recommendation)
    }


    private fun initRecycler() {
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
                if(!foundContacts) {
                    showRecyclerViewUI()
                    hideNoResultsFoundUI()
                }
                viewModel.usersLiveData.value = viewModel.usersLiveData.value //todo replace it
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
                viewModel.clearSearchedContacts()
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