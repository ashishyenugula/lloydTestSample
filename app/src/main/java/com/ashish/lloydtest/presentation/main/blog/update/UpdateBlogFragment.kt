package com.ashish.lloydtest.presentation.main.blog.update

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ashish.lloydtest.R
import com.ashish.lloydtest.business.domain.util.StateMessageCallback
import com.ashish.lloydtest.databinding.FragmentUpdateBlogBinding
import com.ashish.lloydtest.presentation.main.blog.BaseBlogFragment
import com.ashish.lloydtest.presentation.main.blog.detail.SHOULD_REFRESH
import com.ashish.lloydtest.presentation.util.processQueue
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


class UpdateBlogFragment : BaseBlogFragment() {

    private val requestOptions = RequestOptions
        .placeholderOf(R.drawable.default_image)
        .error(R.drawable.default_image)

    private val viewModel: UpdateBlogViewModel by viewModels()

    private var _binding: FragmentUpdateBlogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateBlogBinding.inflate(layoutInflater)
        return binding.root
    }

    /*private val cropActivityResultContract = object : ActivityResultContract<Any?, Uri>() {
        override fun createIntent(context: Context, input: Any?): Intent {
            return CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .getIntent(context)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            return CropImage.getActivityResult(intent)?.uriContent
        }
    }*/

    private lateinit var cropActivityResultLauncher: ActivityResultLauncher<Any?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       /* cropActivityResultLauncher = registerForActivityResult(cropActivityResultContract) { uri ->
            viewModel.onTriggerEvent(UpdateBlogEvents.OnUpdateUri(uri))
        }*/
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        subscribeObservers()

        binding.imageContainer.setOnClickListener {
            if (uiCommunicationListener.isStoragePermissionGranted()) {
                cropActivityResultLauncher.launch(null)
            }
        }

        binding.updateTextview.setOnClickListener {
            if (uiCommunicationListener.isStoragePermissionGranted()) {
                cropActivityResultLauncher.launch(null)
            }
        }

    }

    fun subscribeObservers() {
        viewModel.state.observe(viewLifecycleOwner, { state ->
            uiCommunicationListener.displayProgressBar(state.isLoading)
            processQueue(
                context = context,
                queue = state.queue,
                stateMessageCallback = object : StateMessageCallback {
                    override fun removeMessageFromStack() {
                        viewModel.onTriggerEvent(UpdateBlogEvents.OnRemoveHeadFromQueue)
                    }
                })
            state.blogPost?.let { blogPost ->
                val image = state.newImageUri
                setBlogProperties(
                    blogPost.title,
                    blogPost.body,
                    image ?: blogPost.image.toUri()
                )
            }
            if (state.isUpdateComplete) {
                findNavController().previousBackStackEntry?.savedStateHandle?.set(
                    SHOULD_REFRESH,
                    true
                )
                findNavController().popBackStack(R.id.viewBlogFragment, false)
            }
        })
    }

    private fun setBlogProperties(title: String?, body: String?, image: Uri?) {
        image?.let {
            Glide.with(this)
                .setDefaultRequestOptions(requestOptions)
                .load(it)
                .into(binding.blogImage)
        }
        binding.blogTitle.setText(title)
        binding.blogBody.setText(body)
    }

    private fun saveChanges() {
        cacheState()
        viewModel.onTriggerEvent(UpdateBlogEvents.Update)
        uiCommunicationListener.hideSoftKeyboard()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                saveChanges()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun cacheState() {
        val title = binding.blogTitle.text.toString()
        val body = binding.blogBody.text.toString()
        viewModel.onTriggerEvent(UpdateBlogEvents.OnUpdateTitle(title))
        viewModel.onTriggerEvent(UpdateBlogEvents.OnUpdateBody(body))
    }

    override fun onPause() {
        super.onPause()
        cacheState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}










