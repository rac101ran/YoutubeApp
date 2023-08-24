package com.example.youtubeapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.youtubeapp.databinding.FragmentWatchLaterBinding
import com.example.youtubeapp.databinding.VideoCardBinding
import com.example.youtubeapp.databinding.WatchLaterCardBinding
import com.example.youtubeapp.repository.VideoData
import com.example.youtubeapp.utils.GenericAdapter
import com.example.youtubeapp.viewmodel.VideoViewModel
import kotlinx.coroutines.launch


class WatchLaterFragment : Fragment() {

    private lateinit var videoViewModel: VideoViewModel
    private lateinit var videoAdapter: GenericAdapter<VideoData>
    private lateinit var watchLaterMainList: MutableList<VideoData>
    private lateinit var binding: FragmentWatchLaterBinding
    private var userId: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWatchLaterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        watchLaterMainList = mutableListOf()
        videoViewModel = ViewModelProvider(this)[VideoViewModel::class.java]
        userId = context?.getSharedPreferences("com.example.youtubeapp", Context.MODE_PRIVATE)?.getInt("id",-1)

        videoAdapter = GenericAdapter(watchLaterMainList,
            bindingInflater = { inflater, parent, attachToParent ->
                WatchLaterCardBinding.inflate(inflater, parent, attachToParent)
            },
            onBind = { binding, video ->
                // Bind data to the layout using view binding
                val watchLaterBinding = binding as WatchLaterCardBinding

                // Load thumbnail image using Picasso and view binding
                watchLaterBinding.watchLaterVideoTitle.text = video.title

                watchLaterBinding.deleteFromWatchLaterId.setOnClickListener {
                    viewLifecycleOwner.lifecycleScope.launch {
                        Log.e("message","remove clicked...")
                        val response =
                            videoViewModel.deleteFromWatchLaterList(video.videoId, userId)
                        if (response != null && response.code == 200) {
                            Log.e("message",response.code.toString())
                            watchLaterMainList.remove(video)
                            videoAdapter.updateList(watchLaterMainList)
                        }
                    }
                }

                Glide.with(this)
                    .load(video.thumbnailUrl.removeRange(4, 5))
                    .into(binding.watchlaterImage)

                binding.executePendingBindings()
            }
        )

        binding.watchLaterRecyclerViewId.layoutManager = LinearLayoutManager(context)
        binding.watchLaterRecyclerViewId.adapter = videoAdapter

        videoViewModel.watchLaterList.observe(viewLifecycleOwner) { videos ->
            watchLaterMainList.clear()
            watchLaterMainList.addAll(videos)
            videoAdapter.updateList(watchLaterMainList)
        }

        videoViewModel.getAllWatchLaterVideos(userId)

    }

}