package com.example.c001apk.ui.fragment.topic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.c001apk.R
import com.example.c001apk.databinding.FragmentTopicBinding
import com.example.c001apk.ui.fragment.BlankFragment
import com.google.android.material.tabs.TabLayoutMediator

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class TopicFragment : Fragment() {

    private lateinit var binding: FragmentTopicBinding
    private val viewModel by lazy { ViewModelProvider(this)[TopicViewModel::class.java] }
    private lateinit var param1: String
    private var param2: String? = null
    private val tabList = ArrayList<String>()
    private var fragmentList = ArrayList<Fragment>()

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TopicFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)!!
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTopicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getViewData()

        viewModel.topicLayoutLiveData.observe(viewLifecycleOwner) { result ->
            val data = result.getOrNull()
            if (data != null) {
                for (element in data.tabList)
                    tabList.add(element.title)
                for (i in 0 until data.tabList.size)
                    fragmentList.add(BlankFragment())
                binding.progress.isIndeterminate = false

                binding.toolBar.apply {
                    title = param1
                    subtitle = data.intro
                    //tooltipText = data.intro
                    setNavigationIcon(R.drawable.ic_back)
                    setNavigationOnClickListener {
                        requireActivity().finish()
                    }
                }

                binding.viewPager.adapter = object : FragmentStateAdapter(this) {
                    override fun createFragment(position: Int) = fragmentList[position]
                    override fun getItemCount() = tabList.size
                }
                TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                    tab.text = tabList[position]
                }.attach()
            } else {
                //binding.progress.isIndeterminate = false
                result.exceptionOrNull()?.printStackTrace()
            }
        }

    }

    private fun getViewData() {
        viewModel.tag = param1
        viewModel.getTopicLayout()
    }


}