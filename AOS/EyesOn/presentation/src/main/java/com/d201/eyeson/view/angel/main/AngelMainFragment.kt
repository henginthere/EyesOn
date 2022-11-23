package com.d201.eyeson.view.angel.main

import android.Manifest
import android.content.Intent
import android.graphics.Color
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.d201.domain.model.AngelInfo
import com.d201.eyeson.R
import com.d201.eyeson.base.BaseFragment
import com.d201.eyeson.databinding.FragmentAngelMainBinding
import com.d201.eyeson.util.VIEW_ANGEL_HELP
import com.d201.eyeson.view.angel.help.AngelHelpActivity
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


private const val TAG = "AngelMainFragment"

@AndroidEntryPoint
class AngelMainFragment : BaseFragment<FragmentAngelMainBinding>(R.layout.fragment_angel_main) {

    private val angelMainViewModel: AngelMainViewModel by viewModels()

    override fun init() {
        initListener()
        initView()
        initViewModelCallback()
        angelMainViewModel.getAngelInfo()
        actionCheck()
    }

    private fun actionCheck() {
        val bundle = requireActivity().intent.extras
        var action = ""

        if (bundle != null && bundle.containsKey("action")) {
            action = bundle.getString("action")!!
        }

        if (action.isNotEmpty() && action == "AngelHelp") {
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
        }
    }

    private fun initView() {
        binding.apply {
            vm = angelMainViewModel
            initChart()
        }
    }

    private fun initChart() {
        binding.apply {
            pieChart.description.isEnabled = false
            pieChart.holeRadius = 45f
            pieChart.transparentCircleRadius = 50f
            pieChart.animateXY(1000, 1000);

            val l: Legend = pieChart.legend
            l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
            l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
            l.orientation = Legend.LegendOrientation.VERTICAL
            l.setDrawInside(false)
            pieChart.legend.isEnabled = false

            pieChart.setEntryLabelColor(Color.parseColor("#181B68"))
        }
    }

    private fun generatePieData(angelInfo: AngelInfo): PieData? {
        val entries1: ArrayList<PieEntry> = ArrayList()
        entries1.add(PieEntry((angelInfo.compCnt).toFloat(), "민원처리 횟수"))
        entries1.add(PieEntry((angelInfo.helpCnt).toFloat(), "도움 횟수"))
        val ds1 = PieDataSet(entries1, " ")

        val colors: ArrayList<Int> = ArrayList()
        for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)

        ds1.colors = colors
        ds1.sliceSpace = 2f
        ds1.valueTextColor = Color.parseColor("#181B68")
        ds1.valueTextSize = 8f

        val d = PieData(ds1)
        return d
    }

    private fun initViewModelCallback() {
        angelMainViewModel.apply {
            angelInfoSuccess.observe(viewLifecycleOwner) {
                when (it) {
                    true -> updateAngelInfo()
                    false -> {}
                }
            }
        }
    }

    private fun updateAngelInfo() {
        angelMainViewModel.apply{
            binding.apply {
                pieChart.data = generatePieData(angelInfoData.value!!)
                tvHelpCount.text = "${angelInfoData.value!!.helpCnt} 회"
                tvComplaintsCount.text = "${angelInfoData.value!!.compCnt} 회"
            }
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
}