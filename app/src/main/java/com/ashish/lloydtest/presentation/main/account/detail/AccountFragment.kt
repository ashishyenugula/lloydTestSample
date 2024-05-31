package com.ashish.lloydtest.presentation.main.account.detail

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ashish.lloydtest.R
import com.ashish.lloydtest.business.domain.models.Account
import com.ashish.lloydtest.business.domain.util.StateMessageCallback
import com.ashish.lloydtest.databinding.FragmentAccountBinding

import com.ashish.lloydtest.presentation.main.account.BaseAccountFragment
import com.ashish.lloydtest.presentation.util.processQueue

class AccountFragment : BaseAccountFragment() {

    private val viewModel: AccountViewModel by viewModels()

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        binding.changePassword.setOnClickListener{
            findNavController().navigate(R.id.action_accountFragment_to_changePasswordFragment)
        }

        binding.logoutButton.setOnClickListener {
            viewModel.onTriggerEvent(AccountEvents.Logout)
        }

        subscribeObservers()
        viewModel.onTriggerEvent(AccountEvents.GetAccount)
    }

    private fun subscribeObservers(){
        viewModel.state.observe(viewLifecycleOwner) { state ->

            uiCommunicationListener.displayProgressBar(state.isLoading)

            processQueue(
                context = context,
                queue = state.queue,
                stateMessageCallback = object : StateMessageCallback {
                    override fun removeMessageFromStack() {
                        viewModel.onTriggerEvent(AccountEvents.OnRemoveHeadFromQueue)
                    }
                })

            state.account?.let { account ->
                setAccountDataFields(account)
            }
        }
    }

    private fun setAccountDataFields(account: Account){
        binding.email.text = account.email
        binding.username.text = account.username
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_view_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.edit -> {
                viewModel.state.value?.let { state ->
                    state.account?.let { account ->
                        val bundle = bundleOf("accountPk" to account.pk)
                        findNavController().navigate(R.id.action_accountFragment_to_updateAccountFragment, bundle)
                    }
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}