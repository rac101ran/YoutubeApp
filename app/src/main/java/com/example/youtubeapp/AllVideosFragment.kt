package com.example.youtubeapp

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.GeneratedAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.youtubeapp.databinding.FragmentAllVideosBinding
import com.example.youtubeapp.databinding.VideoCardBinding
import com.example.youtubeapp.repository.VideoData
import com.example.youtubeapp.utils.GenericAdapter
import com.example.youtubeapp.viewmodel.VideoViewModel
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class AllVideosFragment : Fragment() {
    private lateinit var videoViewModel: VideoViewModel
    private lateinit var videoAdapter: GenericAdapter<VideoData>
    private lateinit var videoMainList: MutableList<VideoData>
    private var currentPage = 1
    private var isLoading = false
    private lateinit var binding : FragmentAllVideosBinding
    private var userId :Int? = null
    private var totalPages = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAllVideosBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        videoMainList = mutableListOf()

        videoViewModel = ViewModelProvider(this)[VideoViewModel::class.java]

        userId = 1

        videoAdapter = GenericAdapter(videoMainList,
            bindingInflater = { inflater, parent, attachToParent ->
                VideoCardBinding.inflate(inflater, parent, attachToParent)
            },
            onBind = { binding, video ->
                // Bind data to the layout using view binding
                val videoBinding = binding as VideoCardBinding

                // Load thumbnail image using Picasso and view binding
                videoBinding.videoTitleId.text = video.title

                videoBinding.watchLaterId.setOnClickListener {
                    viewLifecycleOwner.lifecycleScope.launch {
                        val response = videoViewModel.addWaterLaterVideo(video.videoId, userId)
                        if (response == 200) {
                            // Video added to watch later, remove it from the list
                            videoMainList.removeAt(videoMainList.indexOf(video))
                            videoAdapter.updateList(videoMainList)
                        }
                    }
                }

                Glide.with(this)
                    .load(video.thumbnailUrl.removeRange(4,5)) // Image to display in case of error
                    .into(binding.imageViewVideoId) // The I

                binding.executePendingBindings()
            }
        )

        binding.rvVideoRecyclerId.layoutManager = LinearLayoutManager(context)
        binding.rvVideoRecyclerId.adapter = videoAdapter

        loadVideos()

        videoViewModel.videoList.observe(viewLifecycleOwner) { videos ->
            isLoading = false
            videoMainList.addAll(videos)
            videoAdapter.updateList(videoMainList)
        }

        videoViewModel.pagesInfo.observe(viewLifecycleOwner) { pages ->
            totalPages = pages.totalPages
        }

        Log.e("pages",totalPages.toString())

        binding.rvVideoRecyclerId.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && visibleItemCount + firstVisibleItemPosition >= totalItemCount) {
                    currentPage++
                    loadVideos()
                    Log.e("pages",totalPages.toString())
                }else {
                    Log.e("here","here")
                }
            }
        })

    }

    private fun loadVideos() {
        isLoading = true
        viewLifecycleOwner.lifecycleScope.launch {
            videoViewModel.getAllVideos(currentPage, PAGE_SIZE, userId)
        }
    }
}