package com.haroldadmin.kshitijchauhan.resumade.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.haroldadmin.kshitijchauhan.resumade.R
import com.haroldadmin.kshitijchauhan.resumade.adapter.EducationAdapter
import com.haroldadmin.kshitijchauhan.resumade.repository.database.Education
import com.haroldadmin.kshitijchauhan.resumade.ui.activities.CreateResumeActivity
import com.haroldadmin.kshitijchauhan.resumade.utilities.areAllItemsSaved
import com.haroldadmin.kshitijchauhan.resumade.utilities.invisible
import com.haroldadmin.kshitijchauhan.resumade.utilities.visible
import com.haroldadmin.kshitijchauhan.resumade.viewmodel.CreateResumeViewModel
import kotlinx.android.synthetic.main.fragment_education.*

class EducationFragment : Fragment() {

    private lateinit var educationAdapter: EducationAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var createResumeViewModel: CreateResumeViewModel
    private lateinit var educationRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        educationAdapter = EducationAdapter(
                { item: Education ->
                    // On Save Button Clicked
                    item.saved = true
                    createResumeViewModel.updateEducation(item)
                },
                { item: Education ->
                    // On Delete Button Clicked
                    createResumeViewModel.deleteEducation(item)
                    (activity as CreateResumeActivity).displaySnackbar("Education deleted.")
                },
                { item: Education ->
                    // On Edit Button Clicked
                    item.saved = false
                    createResumeViewModel.updateEducation(item)
                })

        linearLayoutManager = LinearLayoutManager(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_education, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let {
            createResumeViewModel = ViewModelProviders
                    .of(it)
                    .get(CreateResumeViewModel::class.java)
        }

        createResumeViewModel.educationList
                .observe(viewLifecycleOwner, Observer {
                    educationAdapter.updateEducationList(it ?: emptyList())
                    createResumeViewModel.educationDetailsSaved = it == null || it.isEmpty() || it.areAllItemsSaved()
                    toggleNoEducationLayout(it?.size ?: 0)
                })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        educationRecyclerView = view.findViewById(R.id.educationRecyclerView)

        educationRecyclerView.apply {
            adapter = educationAdapter
            layoutManager = linearLayoutManager
        }
    }

    private fun toggleNoEducationLayout(size: Int) {
        if (size > 0) {
            educationRecyclerView.visible()
            noEducationView.invisible()
        } else {
            educationRecyclerView.invisible()
            noEducationView.visible()
        }
    }
}