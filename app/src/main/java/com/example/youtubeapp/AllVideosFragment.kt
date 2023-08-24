package com.example.youtubeapp

import android.annotation.SuppressLint
import android.content.Context
import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AllVideosFragment : Fragment() {
    private lateinit var videoViewModel: VideoViewModel
    private lateinit var videoAdapter: GenericAdapter<VideoData>
    private lateinit var videoMainList: MutableList<VideoData>
    private var currentPage = 1
    private var isLoading = false
    private lateinit var binding: FragmentAllVideosBinding
    private var userId: Int? = null
    private var totalPages = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAllVideosBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        videoMainList = mutableListOf()

        videoViewModel = ViewModelProvider(this)[VideoViewModel::class.java]

        userId = context?.getSharedPreferences("com.example.youtubeapp",Context.MODE_PRIVATE)?.getInt("id",-1)

        Log.e("user id",userId.toString())


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
                        if (response != null && response.code == 201) {
                            // Perform actions after successful addition
                            // For example, navigate to another fragment or update the UI

                            videoMainList.remove(video)
                            videoAdapter.updateList(videoMainList)

//                            requireActivity().supportFragmentManager.beginTransaction()
//                                .replace(R.id.frameLayoutId, WatchLaterFragment()).commit()

                        }
                    }
                }

                Glide.with(this)
                    .load(video.thumbnailUrl.removeRange(4, 5)) // Image to display in case of error
                    .into(binding.imageViewVideoId) // The I

                binding.executePendingBindings()
            }
        )

        binding.rvVideoRecyclerId.layoutManager = LinearLayoutManager(context)
        binding.rvVideoRecyclerId.adapter = videoAdapter


        videoViewModel.videoList.observe(viewLifecycleOwner) { videos ->
            isLoading = false
            videoMainList.addAll(videos)
            videoAdapter.updateList(videoMainList)
            videoAdapter.notifyDataSetChanged()
        }



        videoViewModel.pagesInfo.observe(viewLifecycleOwner) { pages ->
            totalPages = pages.totalPages
        }

        Log.e("pages", totalPages.toString())

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
                    Log.e("pages", totalPages.toString())
                } else {
                    Log.e("here", "here")
                }
            }
        })

        loadVideos()

    }

    private fun loadVideos() {
        isLoading = true
        viewLifecycleOwner.lifecycleScope.launch {
            videoViewModel.getAllVideos(currentPage, PAGE_SIZE, userId)
        }
    }
}