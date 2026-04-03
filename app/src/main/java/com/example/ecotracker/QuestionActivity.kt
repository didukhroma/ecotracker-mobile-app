package com.example.ecotracker

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONArray
import org.json.JSONObject

class QuestionActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ONBOARDING_PAYLOAD = "extra_onboarding_payload"
    }

    private lateinit var prevLink: TextView
    private lateinit var skipLink: TextView
    private lateinit var stepCounterText: TextView
    private lateinit var stepSubtitleText: TextView
    private lateinit var nextButton: MaterialButton

    private lateinit var step1Container: LinearLayout
    private lateinit var step2Container: LinearLayout
    private lateinit var step3Container: LinearLayout
    private lateinit var step4Container: LinearLayout
    private lateinit var step5Container: LinearLayout

    private lateinit var firstNameInputLayout: TextInputLayout
    private lateinit var lastNameInputLayout: TextInputLayout
    private lateinit var countryInputLayout: TextInputLayout
    private lateinit var firstNameInput: TextInputEditText
    private lateinit var lastNameInput: TextInputEditText
    private lateinit var countryDropdown: AutoCompleteTextView

    private lateinit var drivesCarLayout: TextInputLayout
    private lateinit var drivingFrequencyLayout: TextInputLayout
    private lateinit var carTypeLayout: TextInputLayout
    private lateinit var drivesCarDropdown: AutoCompleteTextView
    private lateinit var drivingFrequencyDropdown: AutoCompleteTextView
    private lateinit var carTypeDropdown: AutoCompleteTextView

    private lateinit var dietOptionAll: CheckBox
    private lateinit var dietOptionNoRedMeat: CheckBox
    private lateinit var dietOptionPescatarian: CheckBox
    private lateinit var dietOptionVeggie: CheckBox
    private lateinit var dietErrorText: TextView

    private lateinit var buildingTypeLayout: TextInputLayout
    private lateinit var buildingTypeDropdown: AutoCompleteTextView
    private lateinit var bedroomsValueText: TextView
    private lateinit var peopleValueText: TextView
    private lateinit var bedroomsMinusButton: MaterialButton
    private lateinit var bedroomsPlusButton: MaterialButton
    private lateinit var peopleMinusButton: MaterialButton
    private lateinit var peoplePlusButton: MaterialButton

    private lateinit var goalCutCarbon: CheckBox
    private lateinit var goalSaveMoney: CheckBox
    private lateinit var goalTakeBigSteps: CheckBox
    private lateinit var goalProtectNature: CheckBox
    private lateinit var goalSaveWater: CheckBox
    private lateinit var goalsErrorText: TextView

    private var currentStep = 1
    private val maxStep = 5

    private var selectedCountry: String? = null
    private var selectedDrivesCar: String? = null
    private var selectedDrivingFrequency: String? = null
    private var selectedCarType: String? = null
    private var selectedBuildingType: String? = null

    private var bedrooms = 1
    private var peopleExcludingSelf = 0

    private val answers = OnboardingAnswers()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        bindViews()
        setupDropdowns()
        setupCounters()
        setupListeners()
        renderStep()
        prefillFromFirestoreIfAvailable()
    }

    private fun bindViews() {
        prevLink = findViewById(R.id.prevLink)
        skipLink = findViewById(R.id.skipLink)
        stepCounterText = findViewById(R.id.stepCounterText)
        stepSubtitleText = findViewById(R.id.stepSubtitleText)
        nextButton = findViewById(R.id.nextButton)

        step1Container = findViewById(R.id.step1Container)
        step2Container = findViewById(R.id.step2Container)
        step3Container = findViewById(R.id.step3Container)
        step4Container = findViewById(R.id.step4Container)
        step5Container = findViewById(R.id.step5Container)

        firstNameInputLayout = findViewById(R.id.firstNameInputLayout)
        lastNameInputLayout = findViewById(R.id.lastNameInputLayout)
        countryInputLayout = findViewById(R.id.countryInputLayout)
        firstNameInput = findViewById(R.id.firstNameInput)
        lastNameInput = findViewById(R.id.lastNameInput)
        countryDropdown = findViewById(R.id.countryDropdown)

        drivesCarLayout = findViewById(R.id.drivesCarLayout)
        drivingFrequencyLayout = findViewById(R.id.drivingFrequencyLayout)
        carTypeLayout = findViewById(R.id.carTypeLayout)
        drivesCarDropdown = findViewById(R.id.drivesCarDropdown)
        drivingFrequencyDropdown = findViewById(R.id.drivingFrequencyDropdown)
        carTypeDropdown = findViewById(R.id.carTypeDropdown)

        dietOptionAll = findViewById(R.id.dietOptionAll)
        dietOptionNoRedMeat = findViewById(R.id.dietOptionNoRedMeat)
        dietOptionPescatarian = findViewById(R.id.dietOptionPescatarian)
        dietOptionVeggie = findViewById(R.id.dietOptionVeggie)
        dietErrorText = findViewById(R.id.dietErrorText)

        buildingTypeLayout = findViewById(R.id.buildingTypeLayout)
        buildingTypeDropdown = findViewById(R.id.buildingTypeDropdown)
        bedroomsValueText = findViewById(R.id.bedroomsValueText)
        peopleValueText = findViewById(R.id.peopleValueText)
        bedroomsMinusButton = findViewById(R.id.bedroomsMinusButton)
        bedroomsPlusButton = findViewById(R.id.bedroomsPlusButton)
        peopleMinusButton = findViewById(R.id.peopleMinusButton)
        peoplePlusButton = findViewById(R.id.peoplePlusButton)

        goalCutCarbon = findViewById(R.id.goalCutCarbon)
        goalSaveMoney = findViewById(R.id.goalSaveMoney)
        goalTakeBigSteps = findViewById(R.id.goalTakeBigSteps)
        goalProtectNature = findViewById(R.id.goalProtectNature)
        goalSaveWater = findViewById(R.id.goalSaveWater)
        goalsErrorText = findViewById(R.id.goalsErrorText)
    }

    private fun setupDropdowns() {
        setupDropdown(countryDropdown, R.array.countries) { selectedCountry = it }
        setupDropdown(drivesCarDropdown, R.array.yes_no_options) {
            selectedDrivesCar = it
            val drives = isDrivesCarYes()
            if (!drives) {
                selectedDrivingFrequency = null
                selectedCarType = null
                drivingFrequencyDropdown.setText("", false)
                carTypeDropdown.setText("", false)
                drivingFrequencyLayout.error = null
                carTypeLayout.error = null
            }
            updateDrivingDependentVisibility()
        }
        setupDropdown(drivingFrequencyDropdown, R.array.driving_frequency_options) { selectedDrivingFrequency = it }
        setupDropdown(carTypeDropdown, R.array.car_type_options) { selectedCarType = it }
        setupDropdown(buildingTypeDropdown, R.array.building_type_options) { selectedBuildingType = it }
    }

    private fun setupDropdown(view: AutoCompleteTextView, optionsRes: Int, onSelected: (String) -> Unit) {
        val options = resources.getStringArray(optionsRes)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, options)
        view.setAdapter(adapter)
        view.keyListener = null
        view.setOnClickListener { view.showDropDown() }
        view.setOnItemClickListener { _, _, position, _ ->
            onSelected(options[position])
        }
    }

    private fun setupCounters() {
        updateCounterViews()
        bedroomsMinusButton.setOnClickListener {
            if (bedrooms > 0) bedrooms--
            updateCounterViews()
        }
        bedroomsPlusButton.setOnClickListener {
            bedrooms++
            updateCounterViews()
        }
        peopleMinusButton.setOnClickListener {
            if (peopleExcludingSelf > 0) peopleExcludingSelf--
            updateCounterViews()
        }
        peoplePlusButton.setOnClickListener {
            peopleExcludingSelf++
            updateCounterViews()
        }
    }

    private fun updateCounterViews() {
        bedroomsValueText.text = bedrooms.toString()
        peopleValueText.text = peopleExcludingSelf.toString()

        val bedroomsCanDecrease = bedrooms > 0
        bedroomsMinusButton.isEnabled = bedroomsCanDecrease
        bedroomsMinusButton.alpha = if (bedroomsCanDecrease) 1f else 0.35f

        val peopleCanDecrease = peopleExcludingSelf > 0
        peopleMinusButton.isEnabled = peopleCanDecrease
        peopleMinusButton.alpha = if (peopleCanDecrease) 1f else 0.35f
    }

    private fun setupListeners() {
        prevLink.setOnClickListener {
            if (currentStep > 1) {
                currentStep--
                renderStep()
            } else {
                finish()
            }
        }

        skipLink.setOnClickListener {
            storeCurrentStepAnswers()
            finishOnboarding(completed = false)
        }

        nextButton.setOnClickListener {
            if (validateCurrentStep(showErrors = true)) {
                storeCurrentStepAnswers()
                if (currentStep < maxStep) {
                    currentStep++
                    renderStep()
                } else {
                    finishOnboarding(completed = true)
                }
            }
        }

        firstNameInput.doAfterTextChanged { if (currentStep == 1) firstNameInputLayout.error = null }
        lastNameInput.doAfterTextChanged { if (currentStep == 1) lastNameInputLayout.error = null }
        countryDropdown.doAfterTextChanged {
            if (currentStep == 1) {
                countryInputLayout.error = null
                if (countryDropdown.text.toString() != selectedCountry) selectedCountry = null
            }
        }

        drivesCarDropdown.doAfterTextChanged {
            if (currentStep == 2) {
                drivesCarLayout.error = null
                updateDrivingDependentVisibility()
            }
        }
        drivingFrequencyDropdown.doAfterTextChanged {
            if (currentStep == 2) {
                drivingFrequencyLayout.error = null
            }
        }
        carTypeDropdown.doAfterTextChanged {
            if (currentStep == 2) {
                carTypeLayout.error = null
            }
        }

        val dietListener = {
            if (currentStep == 3 && isAnyDietSelected()) {
                dietErrorText.isVisible = false
            }
        }
        dietOptionAll.setOnCheckedChangeListener { _, _ -> dietListener() }
        dietOptionNoRedMeat.setOnCheckedChangeListener { _, _ -> dietListener() }
        dietOptionPescatarian.setOnCheckedChangeListener { _, _ -> dietListener() }
        dietOptionVeggie.setOnCheckedChangeListener { _, _ -> dietListener() }

        buildingTypeDropdown.doAfterTextChanged {
            if (currentStep == 4) {
                buildingTypeLayout.error = null
                if (buildingTypeDropdown.text.toString() != selectedBuildingType) selectedBuildingType = null
            }
        }

        val goalListener = {
            if (currentStep == 5 && isAnyGoalSelected()) {
                goalsErrorText.isVisible = false
            }
        }
        goalCutCarbon.setOnCheckedChangeListener { _, _ -> goalListener() }
        goalSaveMoney.setOnCheckedChangeListener { _, _ -> goalListener() }
        goalTakeBigSteps.setOnCheckedChangeListener { _, _ -> goalListener() }
        goalProtectNature.setOnCheckedChangeListener { _, _ -> goalListener() }
        goalSaveWater.setOnCheckedChangeListener { _, _ -> goalListener() }
    }

    private fun renderStep() {
        stepCounterText.text = getString(R.string.step_counter_format, currentStep, maxStep)
        prevLink.isVisible = currentStep > 1

        step1Container.isVisible = currentStep == 1
        step2Container.isVisible = currentStep == 2
        step3Container.isVisible = currentStep == 3
        step4Container.isVisible = currentStep == 4
        step5Container.isVisible = currentStep == 5

        stepSubtitleText.text = when (currentStep) {
            1 -> getString(R.string.step_label_about_you)
            2 -> getString(R.string.step_label_driving)
            3 -> getString(R.string.step_label_food)
            4 -> getString(R.string.step_label_home)
            else -> getString(R.string.step_label_goals)
        }

        updateDrivingDependentVisibility()
    }

    private fun updateDrivingDependentVisibility() {
        val drives = isDrivesCarYes()
        drivingFrequencyLayout.isVisible = true
        carTypeLayout.isVisible = true
        drivingFrequencyDropdown.isEnabled = drives
        carTypeDropdown.isEnabled = drives
        drivingFrequencyLayout.isEnabled = drives
        carTypeLayout.isEnabled = drives
        val disabledAlpha = if (drives) 1f else 0.45f
        drivingFrequencyLayout.alpha = disabledAlpha
        carTypeLayout.alpha = disabledAlpha
    }

    private fun isDrivesCarYes(): Boolean {
        val yes = getString(R.string.yes)
        return selectedDrivesCar?.trim()?.equals(yes, ignoreCase = true) == true
    }

    private fun validateCurrentStep(showErrors: Boolean): Boolean {
        return when (currentStep) {
            1 -> validateStep1(showErrors)
            2 -> validateStep2(showErrors)
            3 -> validateStep3(showErrors)
            4 -> validateStep4(showErrors)
            5 -> validateStep5(showErrors)
            else -> true
        }
    }

    private fun validateStep1(showErrors: Boolean): Boolean {
        val firstName = firstNameInput.text?.toString()?.trim().orEmpty()
        val lastName = lastNameInput.text?.toString()?.trim().orEmpty()
        val nameRegex = Regex("^[\\p{L}][\\p{L} '\\-]*$")

        var valid = true
        if (firstName.isBlank()) {
            valid = false
            if (showErrors) firstNameInputLayout.error = getString(R.string.error_empty_first_name)
        } else if (!nameRegex.matches(firstName)) {
            valid = false
            if (showErrors) firstNameInputLayout.error = getString(R.string.error_invalid_first_name)
        } else {
            firstNameInputLayout.error = null
        }

        if (lastName.isBlank()) {
            valid = false
            if (showErrors) lastNameInputLayout.error = getString(R.string.error_empty_last_name)
        } else if (!nameRegex.matches(lastName)) {
            valid = false
            if (showErrors) lastNameInputLayout.error = getString(R.string.error_invalid_last_name)
        } else {
            lastNameInputLayout.error = null
        }

        if (selectedCountry.isNullOrBlank()) {
            valid = false
            if (showErrors) countryInputLayout.error = getString(R.string.error_select_country)
        } else {
            countryInputLayout.error = null
        }

        return valid
    }

    private fun validateStep2(showErrors: Boolean): Boolean {
        var valid = true
        if (selectedDrivesCar.isNullOrBlank()) {
            valid = false
            if (showErrors) drivesCarLayout.error = getString(R.string.error_select_option)
        } else {
            drivesCarLayout.error = null
        }

        val drives = selectedDrivesCar == getString(R.string.yes)
        if (drives) {
            if (selectedDrivingFrequency.isNullOrBlank()) {
                valid = false
                if (showErrors) drivingFrequencyLayout.error = getString(R.string.error_select_option)
            } else {
                drivingFrequencyLayout.error = null
            }
            if (selectedCarType.isNullOrBlank()) {
                valid = false
                if (showErrors) carTypeLayout.error = getString(R.string.error_select_option)
            } else {
                carTypeLayout.error = null
            }
        }
        return valid
    }

    private fun validateStep3(showErrors: Boolean): Boolean {
        val valid = isAnyDietSelected()
        if (showErrors) dietErrorText.isVisible = !valid
        return valid
    }

    private fun validateStep4(showErrors: Boolean): Boolean {
        val valid = !selectedBuildingType.isNullOrBlank()
        if (showErrors) buildingTypeLayout.error = if (!valid) getString(R.string.error_select_building_type) else null
        return valid
    }

    private fun validateStep5(showErrors: Boolean): Boolean {
        val valid = isAnyGoalSelected()
        if (showErrors) goalsErrorText.isVisible = !valid
        return valid
    }

    private fun isAnyDietSelected(): Boolean {
        return dietOptionAll.isChecked || dietOptionNoRedMeat.isChecked || dietOptionPescatarian.isChecked || dietOptionVeggie.isChecked
    }

    private fun isAnyGoalSelected(): Boolean {
        return goalCutCarbon.isChecked || goalSaveMoney.isChecked || goalTakeBigSteps.isChecked || goalProtectNature.isChecked || goalSaveWater.isChecked
    }

    private fun storeCurrentStepAnswers() {
        when (currentStep) {
            1 -> {
                answers.firstName = firstNameInput.text?.toString()?.trim().orEmpty()
                answers.lastName = lastNameInput.text?.toString()?.trim().orEmpty()
                answers.country = selectedCountry.orEmpty()
            }
            2 -> {
                answers.drivesCar = selectedDrivesCar.orEmpty()
                answers.drivingFrequency = selectedDrivingFrequency
                answers.carType = selectedCarType
            }
            3 -> {
                answers.diet = buildList {
                    if (dietOptionAll.isChecked) add(getString(R.string.diet_all))
                    if (dietOptionNoRedMeat.isChecked) add(getString(R.string.diet_no_red_meat))
                    if (dietOptionPescatarian.isChecked) add(getString(R.string.diet_pescatarian))
                    if (dietOptionVeggie.isChecked) add(getString(R.string.diet_veggie))
                }
            }
            4 -> {
                answers.buildingType = selectedBuildingType.orEmpty()
                answers.bedrooms = bedrooms
                answers.peopleExcludingSelf = peopleExcludingSelf
            }
            5 -> {
                answers.goals = buildList {
                    if (goalCutCarbon.isChecked) add(getString(R.string.goal_cut_carbon))
                    if (goalSaveMoney.isChecked) add(getString(R.string.goal_save_money))
                    if (goalTakeBigSteps.isChecked) add(getString(R.string.goal_take_big_steps))
                    if (goalProtectNature.isChecked) add(getString(R.string.goal_protect_nature))
                    if (goalSaveWater.isChecked) add(getString(R.string.goal_save_water))
                }
            }
        }
    }

    private fun finishOnboarding(completed: Boolean) {
        val payload = buildPayloadJson(answers)
        OnboardingSessionStore.latestPayload = payload
        FirebaseSync.saveOnboardingAnswers(this, answers, payload, completed)
        FirebaseSync.syncLearningProgress(this)

        openHome(payload)
    }

    private fun buildPayloadJson(data: OnboardingAnswers): String {
        val json = JSONObject()
            .put("firstName", data.firstName)
            .put("lastName", data.lastName)
            .put("country", data.country)
            .put("drivesCar", data.drivesCar)
            .put("drivingFrequency", data.drivingFrequency.orEmpty())
            .put("carType", data.carType.orEmpty())
            .put("buildingType", data.buildingType)
            .put("bedrooms", data.bedrooms)
            .put("peopleExcludingSelf", data.peopleExcludingSelf)

        json.put("diet", JSONArray(data.diet))
        json.put("goals", JSONArray(data.goals))
        return json.toString()
    }

    private fun openHome(payload: String? = null) {
        startActivity(Intent(this, HomeActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            if (payload != null) putExtra(EXTRA_ONBOARDING_PAYLOAD, payload)
        })
        finishAffinity()
    }

    private fun prefillFromFirestoreIfAvailable() {
        FirebaseSync.fetchOnboardingAnswers(this) { remote ->
            if (remote == null) return@fetchOnboardingAnswers
            runOnUiThread {
                applyPrefill(remote)
            }
        }
    }

    private fun applyPrefill(remote: OnboardingAnswers) {
        answers.firstName = remote.firstName
        answers.lastName = remote.lastName
        answers.country = remote.country
        answers.drivesCar = remote.drivesCar
        answers.drivingFrequency = remote.drivingFrequency
        answers.carType = remote.carType
        answers.diet = remote.diet
        answers.buildingType = remote.buildingType
        answers.bedrooms = remote.bedrooms
        answers.peopleExcludingSelf = remote.peopleExcludingSelf
        answers.goals = remote.goals

        selectedCountry = remote.country.ifBlank { null }
        selectedDrivesCar = remote.drivesCar.ifBlank { null }
        selectedDrivingFrequency = remote.drivingFrequency
        selectedCarType = remote.carType
        selectedBuildingType = remote.buildingType.ifBlank { null }

        firstNameInput.setText(remote.firstName)
        lastNameInput.setText(remote.lastName)
        if (!remote.country.isBlank()) countryDropdown.setText(remote.country, false)
        if (!remote.drivesCar.isBlank()) drivesCarDropdown.setText(remote.drivesCar, false)
        if (!remote.drivingFrequency.isNullOrBlank()) drivingFrequencyDropdown.setText(remote.drivingFrequency, false)
        if (!remote.carType.isNullOrBlank()) carTypeDropdown.setText(remote.carType, false)
        if (!remote.buildingType.isBlank()) buildingTypeDropdown.setText(remote.buildingType, false)

        bedrooms = remote.bedrooms.coerceAtLeast(0)
        peopleExcludingSelf = remote.peopleExcludingSelf.coerceAtLeast(0)
        updateCounterViews()

        dietOptionAll.isChecked = remote.diet.contains(getString(R.string.diet_all))
        dietOptionNoRedMeat.isChecked = remote.diet.contains(getString(R.string.diet_no_red_meat))
        dietOptionPescatarian.isChecked = remote.diet.contains(getString(R.string.diet_pescatarian))
        dietOptionVeggie.isChecked = remote.diet.contains(getString(R.string.diet_veggie))

        goalCutCarbon.isChecked = remote.goals.contains(getString(R.string.goal_cut_carbon))
        goalSaveMoney.isChecked = remote.goals.contains(getString(R.string.goal_save_money))
        goalTakeBigSteps.isChecked = remote.goals.contains(getString(R.string.goal_take_big_steps))
        goalProtectNature.isChecked = remote.goals.contains(getString(R.string.goal_protect_nature))
        goalSaveWater.isChecked = remote.goals.contains(getString(R.string.goal_save_water))

        currentStep = resolveFirstUnfinishedStep(remote)
        renderStep()
        updateDrivingDependentVisibility()
    }

    private fun resolveFirstUnfinishedStep(data: OnboardingAnswers): Int {
        val nameRegex = Regex("^[\\p{L}][\\p{L} '\\-]*$")
        val step1Done = data.firstName.isNotBlank() &&
            data.lastName.isNotBlank() &&
            data.country.isNotBlank() &&
            nameRegex.matches(data.firstName) &&
            nameRegex.matches(data.lastName)
        if (!step1Done) return 1

        val hasDriveChoice = data.drivesCar == getString(R.string.yes) || data.drivesCar == getString(R.string.no)
        val step2Done = if (!hasDriveChoice) {
            false
        } else if (data.drivesCar == getString(R.string.no)) {
            true
        } else {
            !data.drivingFrequency.isNullOrBlank() && !data.carType.isNullOrBlank()
        }
        if (!step2Done) return 2

        val step3Done = data.diet.isNotEmpty()
        if (!step3Done) return 3

        val step4Done = data.buildingType.isNotBlank()
        if (!step4Done) return 4

        return 5
    }
}

data class OnboardingAnswers(
    var firstName: String = "",
    var lastName: String = "",
    var country: String = "",
    var drivesCar: String = "",
    var drivingFrequency: String? = null,
    var carType: String? = null,
    var diet: List<String> = emptyList(),
    var buildingType: String = "",
    var bedrooms: Int = 1,
    var peopleExcludingSelf: Int = 0,
    var goals: List<String> = emptyList()
)

object OnboardingSessionStore {
    var latestPayload: String? = null
}
