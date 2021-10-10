package com.shpp.eorlov.assignment1.ui.addContacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.shpp.eorlov.assignment1.R
import com.shpp.eorlov.assignment1.base.BaseFragment
import com.shpp.eorlov.assignment1.databinding.FragmentAddContactsBinding
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.ui.SharedViewModel
import com.shpp.eorlov.assignment1.ui.addContacts.adapter.AddContactsRecyclerAdapter
import com.shpp.eorlov.assignment1.ui.addContacts.adapter.listeners.IAddContactClickListener
import com.shpp.eorlov.assignment1.ui.details.DetailViewFragmentArgs
import com.shpp.eorlov.assignment1.utils.ext.clickWithDebounce
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
        initRecycler()
        setObservers()
        setListeners()
    }

    override fun onResume() {
        super.onResume()
        printLog("On resume")
    }

    override fun onCheckedContacts(contact: UserModel) {
        if (binding.textViewTitle.text != getString(R.string.recommendation)) {
            binding.textViewTitle.text = getString(R.string.recommendation)
        }
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
        }
    }

    private fun setObservers() {
        viewModel.apply {
            usersLiveData.observe(viewLifecycleOwner) {
                contactsListAdapter.submitList(it.toMutableList())
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
}