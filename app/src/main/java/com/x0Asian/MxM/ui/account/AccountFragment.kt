package com.x0Asian.MxM.ui.account

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText // For the EditText fields
import android.widget.TextView // For the "Edit" TextView
import androidx.fragment.app.Fragment
import com.x0Asian.MxM.R // Correct R import

class AccountFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_account, container, false)

        // Find the "Edit" TextView by its ID
        val editButton: TextView = view.findViewById(R.id.textViewEdit)

        // Find all the EditText views by their IDs
        val mentorSkillsEditText: EditText = view.findViewById(R.id.editTextMentorSkills)
        val mentorExperienceEditText: EditText = view.findViewById(R.id.editTextMentorExperience)
        val menteeSkillsEditText: EditText = view.findViewById(R.id.editTextMenteeSkills)
        val menteeExperienceEditText: EditText = view.findViewById(R.id.editTextMenteeExperience)

        // Set an OnClickListener on the "Edit" button
        editButton.setOnClickListener {
            // When the "Edit" button is clicked, get the text from all the EditText fields
            val mentorSkills = mentorSkillsEditText.text.toString()
            val mentorExperience = mentorExperienceEditText.text.toString()
            val menteeSkills = menteeSkillsEditText.text.toString()
            val menteeExperience = menteeExperienceEditText.text.toString()

            // For now, let's log the values to the console to confirm they are being captured.
            // In a real app, you would save this data to a database.
            Log.d("AccountFragment", "Mentor Skills: $mentorSkills")
            Log.d("AccountFragment", "Mentor Experience: $mentorExperience")
            Log.d("AccountFragment", "Mentee Skills: $menteeSkills")
            Log.d("AccountFragment", "Mentee Experience: $menteeExperience")
        }

        return view
    }
}