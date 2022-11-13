package com.marvastsi.mediaplayer

import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.MediaController
import androidx.navigation.fragment.findNavController
import com.marvastsi.mediaplayer.databinding.FragmentVideoBinding

class MediaFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private lateinit var mediaController: MediaController
    private var _binding: FragmentVideoBinding? = null
    private val binding get() = _binding!!
    private var mediaPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("onCreate()")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoBinding.inflate(inflater, container, false)
        val uriString = mediaPath ?: getMediaPath("Video")
        val uri = Uri.parse(uriString)
        val mVideoView = binding.mediaView
        val spinner = binding.filesSpinner

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.media_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = this

        mediaController = MediaController(activity)
        mediaController.setAnchorView(mVideoView)
        mVideoView.setMediaController(mediaController)
        mVideoView.setVideoURI(uri)
        mVideoView.requestFocus()
        mVideoView.start()

        mVideoView.setOnCompletionListener {
            navigateBack()
        }

        return binding.root
    }

    private fun navigateBack() {
        findNavController().navigate(R.id.action_VideoFragment_to_FirstFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("onViewCreated()")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selectedItemText = parent?.getItemAtPosition(position)
        if(position > 0)
            mediaPath = getMediaPath(selectedItemText)
    }

    private fun getMediaPath(item: Any?): String {
        // Should use position (index) instead itemText (value)
        val packageName = javaClass.getPackage()?.name
        return when (item) {
            "Video" -> "android.resource://$packageName/${R.raw.video}"
            "Song" -> "android.resource://$packageName/${R.raw.song}"
            "YouTube" -> "https://www.youtube.com/watch?v=jxV_8zb_YD8"
            else -> "android.resource://$packageName/${R.raw.video}"
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}