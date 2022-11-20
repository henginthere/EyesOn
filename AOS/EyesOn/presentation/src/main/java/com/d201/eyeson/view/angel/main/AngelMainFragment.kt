package com.d201.eyeson.view.angel.main

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseFragment
import com.d201.eyeson.databinding.FragmentAngelMainBinding
import com.d201.eyeson.util.VIEW_ANGEL_HELP
import com.d201.eyeson.view.angel.ComplaintsClickListener
import com.d201.eyeson.view.angel.help.AngelHelpActivity
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


private const val TAG = "AngelMainFragment"

@AndroidEntryPoint
class AngelMainFragment : BaseFragment<FragmentAngelMainBinding>(R.layout.fragment_angel_main) {

    private val angelMainViewModel: AngelMainViewModel by viewModels()
    private lateinit var job: Job
    private lateinit var angelMainAdapter: AngelMainAdapter

    override fun init() {
        initListener()
        initView()
        initViewModelCallback()
        angelMainViewModel.getAngelInfo()
        actionCheck()
    }

    override fun onResume() {
        super.onResume()
        angelMainViewModel.getAngelInfo()
    }

    private fun actionCheck() {
        val bundle = requireActivity().intent.extras
        var action = ""

        if (bundle != null && bundle.containsKey("action")) {
            action = bundle.getString("action")!!
        }

        if (action.isNotEmpty() && action == "AngelHelp") {
            Log.d(TAG, "actionCheck: $action")
            requireActivity().intent.putExtra("action", "")
            checkPermission(
                VIEW_ANGEL_HELP,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.MODIFY_AUDIO_SETTINGS
            )
        }
    }

    private fun initListener() {
        binding.apply {
            btnSetting.setOnClickListener {
                findNavController().navigate(AngelMainFragmentDirections.actionAngelMainFragmentToAngelSettingFragment())
            }

        }
    }

    private fun initView() {
        angelMainAdapter = AngelMainAdapter(complaintsClickListener)
        binding.apply {
            vm = angelMainViewModel

            btnWaitingComplaints.setOnClickListener {
                findNavController().navigate(
                    AngelMainFragmentDirections.actionAngelMainFragmentToComplaintsListFragment()
                )
            }

            btnMyComplaints.setOnClickListener {
                findNavController().navigate(
                    AngelMainFragmentDirections.actionAngelMainFragmentToComplaintAngelListFragment()
                )
            }

            constraintLayoutGuide.setOnClickListener {
                showToast("준비 중인 기능입니다")
            }



            pieChart.getDescription().setEnabled(false)
            pieChart.setHoleRadius(45f)
            pieChart.setTransparentCircleRadius(50f)
            pieChart.animateXY(1000, 1000);
            val l: Legend = pieChart.getLegend()
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP)
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT)
            l.setOrientation(Legend.LegendOrientation.VERTICAL)
            l.setDrawInside(false)
            pieChart.legend.isEnabled = false

            pieChart.setEntryLabelColor(Color.parseColor("#181B68"))
            pieChart.setData(generatePieData())
        }
    }

    private fun generatePieData(): PieData? {

        val entries1: ArrayList<PieEntry> = ArrayList()
        entries1.add(PieEntry((65).toFloat(), "최근 민원 수"))
        entries1.add(PieEntry((17).toFloat(), "나의 민원 수"))
        entries1.add(PieEntry((54).toFloat(), "최근 도움 수"))
        entries1.add(PieEntry((6).toFloat(), "나의 도움 수"))
//        entries1.add(PieEntry((40).toFloat(), ""))
        val ds1 = PieDataSet(entries1, " ")

        val colors: ArrayList<Int> = ArrayList()
        for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)

        ds1.setColors(colors)
        ds1.setSliceSpace(2f)
        ds1.setValueTextColor(Color.parseColor("#181B68"))
        ds1.setValueTextSize(8f)

        val d = PieData(ds1)
      //  d.setValueTypeface(tf)
        return d
    }

    private fun initViewModelCallback() {
        job = lifecycleScope.launch {
            angelMainViewModel.complaintsList.collectLatest {
                if (it != null) {
                    angelMainAdapter.submitData(it)
                }
            }
        }
        lifecycleScope.launch {
            angelMainViewModel.apply {
                angelInfoData.collectLatest {
                }
            }
        }
        angelMainViewModel.getComplaintsList()

    }

    private val complaintsClickListener = object : ComplaintsClickListener {
        override fun onClick(complaintsSeq: Long) {
            findNavController().navigate(
                AngelMainFragmentDirections.actionAngelMainFragmentToComplaintsDetailFragment(
                    complaintsSeq
                )
            )
        }
    }

    private fun checkPermission(direction: Int, vararg permissions: String) {
        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                when (direction) {
                    VIEW_ANGEL_HELP -> startActivity(
                        Intent(
                            requireActivity(),
                            AngelHelpActivity::class.java
                        )
                    )
                }
            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {
                showToast("권한을 허용해야 이용이 가능합니다.")
            }

        }
        TedPermission.create()
            .setPermissionListener(permissionListener)
            .setDeniedMessage("권한을 허용해주세요. [설정] > [앱 및 알림] > [고급] > [앱 권한]")
            .setPermissions(
                *permissions
            )
            .check()
    }

    private fun generateCenterText(): SpannableString? {
        val s = SpannableString("Revenues\nQuarters 2015")
        s.setSpan(RelativeSizeSpan(2f), 0, 8, 0)
        s.setSpan(ForegroundColorSpan(Color.GRAY), 8, s.length, 0)
        return s
    }
}