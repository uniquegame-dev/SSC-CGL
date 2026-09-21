package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BorderMedium
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CorrectGreen
import com.example.ui.theme.CorrectGreenBg
import com.example.ui.theme.CorrectGreenBorder
import com.example.ui.theme.DifficultyEasy
import com.example.ui.theme.DifficultyEasyBg
import com.example.ui.theme.DifficultyModerate
import com.example.ui.theme.DifficultyModerateBg
import com.example.ui.theme.LightBackground
import com.example.ui.theme.LightCardBackground
import com.example.ui.theme.LightCardBackgroundVariant
import com.example.ui.theme.LightSurface
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.PrimaryBlueDark
import com.example.ui.theme.PrimaryBlueLight
import com.example.ui.theme.TextDarkBody
import com.example.ui.theme.TextDarkHeading
import com.example.ui.theme.TextMediumGray

// -------------------------------------------------------------
// PYQ Data Models & Repositories
// -------------------------------------------------------------

data class PyqYearSummary(
    val year: Int,
    val examName: String,
    val totalPapers: Int,
    val examDateRange: String,
    val patternType: String, // "New Pattern (CBT)", "CBT", "OMR / Offline"
    val badge: String
)

data class PyqPaperShiftItem(
    val id: String,
    val year: Int,
    val examDate: String,
    val shiftName: String,
    val timeSlot: String,
    val paperTitle: String,
    val totalQuestions: Int = 100,
    val maxMarks: Int = 200,
    val durationMinutes: Int = 60,
    val correctMarks: String = "+2.0",
    val negativeMarks: String = "−0.50",
    val status: String = "Blueprint Ready"
)

object PyqDataRepository {
    // Years from 2025 down to 2010 (16 Years)
    val PYQ_YEARS: List<PyqYearSummary> = listOf(
        PyqYearSummary(2025, "SSC CGL 2025 Tier I", 36, "Sep 2025 - Oct 2025", "New Pattern (CBT)", "Solved Papers"),
        PyqYearSummary(2024, "SSC CGL 2024 Tier I", 39, "09 Sep 2024 - 26 Sep 2024", "New Pattern (CBT)", "Official CBT"),
        PyqYearSummary(2023, "SSC CGL 2023 Tier I", 39, "14 Jul 2023 - 27 Jul 2023", "New Pattern (CBT)", "Official CBT"),
        PyqYearSummary(2022, "SSC CGL 2022 Tier I", 40, "01 Dec 2022 - 13 Dec 2022", "New Pattern (CBT)", "Official CBT"),
        PyqYearSummary(2021, "SSC CGL 2021 Tier I", 21, "11 Apr 2022 - 21 Apr 2022", "Standard CBT", "Official CBT"),
        PyqYearSummary(2020, "SSC CGL 2020 Tier I", 21, "13 Aug 2021 - 24 Aug 2021", "Standard CBT", "Official CBT"),
        PyqYearSummary(2019, "SSC CGL 2019 Tier I", 18, "03 Mar 2020 - 09 Mar 2020", "Standard CBT", "Official CBT"),
        PyqYearSummary(2018, "SSC CGL 2018 Tier I", 21, "04 Jun 2019 - 13 Jun 2019", "Standard CBT", "Official CBT"),
        PyqYearSummary(2017, "SSC CGL 2017 Tier I", 43, "05 Aug 2017 - 23 Aug 2017", "Standard CBT", "Official CBT"),
        PyqYearSummary(2016, "SSC CGL 2016 Tier I", 44, "27 Aug 2016 - 11 Sep 2016", "First CBT Pattern", "Historic CBT"),
        PyqYearSummary(2015, "SSC CGL 2015 Tier I", 8, "09 Aug 2015 - 16 Aug 2015", "OMR (Offline)", "Historic Paper"),
        PyqYearSummary(2014, "SSC CGL 2014 Tier I", 6, "19 Oct 2014 - 26 Oct 2014", "OMR (Offline)", "Historic Paper"),
        PyqYearSummary(2013, "SSC CGL 2013 Tier I", 6, "21 Apr 2013 - 19 May 2013", "OMR (Offline)", "Historic Paper"),
        PyqYearSummary(2012, "SSC CGL 2012 Tier I", 4, "01 Jul 2012 - 08 Jul 2012", "OMR (Offline)", "Historic Paper"),
        PyqYearSummary(2011, "SSC CGL 2011 Tier I", 4, "19 Jun 2011 - 26 Jun 2011", "OMR (Offline)", "Historic Paper"),
        PyqYearSummary(2010, "SSC CGL 2010 Tier I", 4, "16 May 2010 - 23 May 2010", "OMR (Offline)", "Historic Paper")
    )

    fun getYearSummary(year: Int): PyqYearSummary {
        return PYQ_YEARS.find { it.year == year } ?: PyqYearSummary(
            year = year,
            examName = "SSC CGL $year Tier I",
            totalPapers = 12,
            examDateRange = "Exam Held in $year",
            patternType = if (year >= 2016) "CBT Online" else "OMR Offline",
            badge = "$year Papers"
        )
    }

    fun getShiftsForYear(year: Int): List<PyqPaperShiftItem> {
        val shifts = listOf(
            Pair("Shift 1", "09:00 AM – 10:00 AM"),
            Pair("Shift 2", "12:30 PM – 01:30 PM"),
            Pair("Shift 3", "04:00 PM – 05:00 PM"),
            Pair("Shift 4", "05:30 PM – 06:30 PM")
        )

        val days = when (year) {
            2025 -> listOf("10 Sep 2025", "11 Sep 2025", "12 Sep 2025", "15 Sep 2025")
            2024 -> listOf("09 Sep 2024", "10 Sep 2024", "11 Sep 2024", "12 Sep 2024", "13 Sep 2024", "17 Sep 2024")
            2023 -> listOf("14 Jul 2023", "17 Jul 2023", "18 Jul 2023", "19 Jul 2023", "20 Jul 2023", "21 Jul 2023")
            2022 -> listOf("01 Dec 2022", "02 Dec 2022", "03 Dec 2022", "05 Dec 2022", "06 Dec 2022", "07 Dec 2022")
            2021 -> listOf("11 Apr 2022", "12 Apr 2022", "13 Apr 2022", "18 Apr 2022", "19 Apr 2022")
            2020 -> listOf("13 Aug 2021", "16 Aug 2021", "17 Aug 2021", "18 Aug 2021")
            2019 -> listOf("03 Mar 2020", "04 Mar 2020", "05 Mar 2020", "06 Mar 2020")
            2018 -> listOf("04 Jun 2019", "06 Jun 2019", "07 Jun 2019", "10 Jun 2019")
            2017 -> listOf("05 Aug 2017", "06 Aug 2017", "08 Aug 2017", "09 Aug 2017")
            2016 -> listOf("27 Aug 2016", "28 Aug 2016", "29 Aug 2016", "30 Aug 2016")
            2015 -> listOf("09 Aug 2015 (Morning)", "09 Aug 2015 (Evening)", "16 Aug 2015 (Morning)", "16 Aug 2015 (Evening)")
            2014 -> listOf("19 Oct 2014 (Morning)", "19 Oct 2014 (Evening)", "26 Oct 2014 (Morning)", "26 Oct 2014 (Evening)")
            2013 -> listOf("21 Apr 2013", "19 May 2013")
            2012 -> listOf("01 Jul 2012", "08 Jul 2012")
            2011 -> listOf("19 Jun 2011", "26 Jun 2011")
            2010 -> listOf("16 May 2010", "23 May 2010")
            else -> listOf("Day 1", "Day 2", "Day 3")
        }

        val items = mutableListOf<PyqPaperShiftItem>()
        var count = 1

        for (day in days) {
            val shiftCount = if (year <= 2015) 2 else 3
            for (i in 0 until shiftCount) {
                val shift = shifts[i]
                items.add(
                    PyqPaperShiftItem(
                        id = "pyq_${year}_${count}",
                        year = year,
                        examDate = day,
                        shiftName = shift.first,
                        timeSlot = shift.second,
                        paperTitle = "SSC CGL $year Tier-I Official Paper (Set $count)",
                        totalQuestions = if (year <= 2015) 200 else 100,
                        maxMarks = 200,
                        durationMinutes = if (year <= 2015) 120 else 60,
                        correctMarks = if (year <= 2015) "+1.0" else "+2.0",
                        negativeMarks = if (year <= 2015) "−0.25" else "−0.50",
                        status = "Paper Structure Loaded"
                    )
                )
                count++
            }
        }

        return items
    }
}

// -------------------------------------------------------------
// 1. PYQ Years List Screen (2026 down to 2010)
// -------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PyqYearsScreen(
    onSelectYear: (year: Int) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedFilter by rememberSaveable { mutableStateOf("All") }

    BackHandler {
        onBack()
    }

    val allYears = remember { PyqDataRepository.PYQ_YEARS }
    val filteredYears = remember(searchQuery, selectedFilter) {
        allYears.filter { item ->
            val matchesSearch = searchQuery.isBlank() ||
                item.year.toString().contains(searchQuery.trim()) ||
                item.examName.contains(searchQuery.trim(), ignoreCase = true)

            val matchesFilter = when (selectedFilter) {
                "Latest (2020-2025)" -> item.year >= 2020
                "2015-2019" -> item.year in 2015..2019
                "Historic (2010-2014)" -> item.year in 2010..2014
                else -> true
            }

            matchesSearch && matchesFilter
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("pyq_years_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "SSC CGL PYQ Papers",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp
                            ),
                            color = TextDarkHeading
                        )
                        Text(
                            text = "Previous Year Papers (2025 – 2010)",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = PrimaryBlue
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("pyq_years_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to Test Menu",
                            tint = TextDarkHeading
                        )
                    }
                },
                actions = {
                    OfflineBadge()
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = LightBackground,
                    titleContentColor = TextDarkHeading
                )
            )
        },
        containerColor = LightBackground
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                modifier = Modifier
                    .widthIn(max = 560.dp)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 10.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header Info Banner
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = LightCardBackgroundVariant),
                        border = BorderStroke(1.2.dp, PrimaryBlue.copy(alpha = 0.25f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("pyq_banner_card")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = PrimaryBlue,
                                modifier = Modifier.size(42.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.History,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "16 Years Exam Archive",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    ),
                                    color = PrimaryBlueDark
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Select any year to explore date-wise shifts, paper structures, and marking schemes.",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 12.sp,
                                        lineHeight = 17.sp
                                    ),
                                    color = TextDarkBody
                                )
                            }
                        }
                    }
                }

                // Filter & Search Controls
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Search Box
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = {
                                Text(
                                    text = "Search by year (e.g. 2024, 2023)...",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                                    color = TextMediumGray
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    tint = PrimaryBlue,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = LightSurface,
                                unfocusedContainerColor = LightSurface,
                                focusedBorderColor = PrimaryBlue,
                                unfocusedBorderColor = BorderLight
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("pyq_search_input")
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Filter Chips
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            val filterOptions = listOf(
                                "All",
                                "Latest (2020-2025)",
                                "2015-2019",
                                "Historic (2010-2014)"
                            )

                            items(filterOptions) { filter ->
                                val isSelected = selectedFilter == filter
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { selectedFilter = filter },
                                    label = {
                                        Text(
                                            text = filter,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                            )
                                        )
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = PrimaryBlue,
                                        selectedLabelColor = Color.White,
                                        containerColor = LightSurface,
                                        labelColor = TextDarkBody
                                    ),
                                    border = FilterChipDefaults.filterChipBorder(
                                        borderColor = if (isSelected) PrimaryBlue else BorderLight,
                                        selectedBorderColor = PrimaryBlue,
                                        enabled = true,
                                        selected = isSelected
                                    ),
                                    modifier = Modifier.testTag("pyq_filter_${filter.lowercase().replace(" ", "_")}")
                                )
                            }
                        }
                    }
                }

                // Section Label
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Available Exam Years (${filteredYears.size})",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            ),
                            color = TextDarkHeading
                        )
                        Text(
                            text = "2025 ↓ 2010",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = PrimaryBlue
                            )
                        )
                    }
                }

                // Year Cards List
                items(filteredYears, key = { it.year }) { yearSummary ->
                    PyqYearCard(
                        summary = yearSummary,
                        onClick = { onSelectYear(yearSummary.year) }
                    )
                }
            }
        }
    }
}

@Composable
private fun PyqYearCard(
    summary: PyqYearSummary,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LightSurface),
        border = BorderStroke(1.2.dp, BorderLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag("pyq_year_card_${summary.year}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Big Year Badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = LightCardBackgroundVariant,
                    border = BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.25f)),
                    modifier = Modifier.size(54.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "${summary.year}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp
                            ),
                            color = PrimaryBlueDark
                        )
                        Text(
                            text = "Tier I",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 9.sp
                            ),
                            color = PrimaryBlue
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = summary.examName,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            ),
                            color = TextDarkHeading
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = if (summary.year >= 2024) CorrectGreenBg else LightCardBackground,
                            border = BorderStroke(1.dp, if (summary.year >= 2024) CorrectGreenBorder else BorderLight)
                        ) {
                            Text(
                                text = summary.badge,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 9.5.sp,
                                    color = if (summary.year >= 2024) CorrectGreen else TextMediumGray
                                ),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "🗓 ${summary.examDateRange}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 11.5.sp,
                                color = TextMediumGray
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "📄 ${summary.totalPapers} Shift Papers",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 11.sp,
                                color = PrimaryBlueDark
                            )
                        )
                        Text(
                            text = " • ${summary.patternType}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 11.sp,
                                color = TextMediumGray
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Surface(
                shape = CircleShape,
                color = LightCardBackgroundVariant,
                modifier = Modifier.size(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Open Year Details",
                        tint = PrimaryBlue,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 2. PYQ Year Detail Screen (Shifts & Paper Info Placeholders)
// -------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PyqYearDetailScreen(
    year: Int,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val yearSummary = remember(year) { PyqDataRepository.getYearSummary(year) }
    val shiftPapers = remember(year) { PyqDataRepository.getShiftsForYear(year) }
    var selectedShiftFilter by rememberSaveable { mutableStateOf("All Shifts") }

    BackHandler {
        onBack()
    }

    val filteredPapers = remember(shiftPapers, selectedShiftFilter) {
        if (selectedShiftFilter == "All Shifts") {
            shiftPapers
        } else {
            shiftPapers.filter { it.shiftName.contains(selectedShiftFilter, ignoreCase = true) }
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("pyq_year_detail_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "SSC CGL $year PYQ",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp
                            ),
                            color = TextDarkHeading
                        )
                        Text(
                            text = "Shift-Wise Papers & Structure",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = PrimaryBlue
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("pyq_detail_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to Years",
                            tint = TextDarkHeading
                        )
                    }
                },
                actions = {
                    OfflineBadge()
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = LightBackground,
                    titleContentColor = TextDarkHeading
                )
            )
        },
        containerColor = LightBackground
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                modifier = Modifier
                    .widthIn(max = 560.dp)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 10.dp, bottom = 28.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Year Overview Hero Card
                item {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = LightSurface),
                        border = BorderStroke(1.2.dp, BorderLight),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("pyq_year_overview_hero_card")
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = LightCardBackgroundVariant,
                                        border = BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.25f))
                                    ) {
                                        Text(
                                            text = "EXAM ARCHIVE • ${yearSummary.patternType.uppercase()}",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 10.sp,
                                                color = PrimaryBlueDark
                                            ),
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = yearSummary.examName,
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 20.sp
                                        ),
                                        color = TextDarkHeading
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Exam Window: ${yearSummary.examDateRange}",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontSize = 12.sp,
                                            color = TextMediumGray
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider(color = BorderLight)
                            Spacer(modifier = Modifier.height(14.dp))

                            // 4 Key Metrics Row (Shifts, Questions, Marks, Duration)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                MetricInfoPill(
                                    label = "Shifts",
                                    value = "${shiftPapers.size}",
                                    icon = Icons.Default.Layers,
                                    modifier = Modifier.weight(1f)
                                )
                                MetricInfoPill(
                                    label = "Questions",
                                    value = if (year <= 2015) "200 Qs" else "100 Qs",
                                    icon = Icons.Default.Quiz,
                                    modifier = Modifier.weight(1f)
                                )
                                MetricInfoPill(
                                    label = "Max Marks",
                                    value = "200",
                                    icon = Icons.Default.Speed,
                                    modifier = Modifier.weight(1f)
                                )
                                MetricInfoPill(
                                    label = "Duration",
                                    value = if (year <= 2015) "120 min" else "60 min",
                                    icon = Icons.Default.Timer,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                // Section Scheme Pill Banner
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = LightCardBackgroundVariant),
                        border = BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.2f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Standard 4 Sections:",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryBlueDark
                                )
                            )
                            Text(
                                text = "Reasoning (25) • GA (25) • Quant (25) • English (25)",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = TextDarkHeading
                                )
                            )
                        }
                    }
                }

                // Shift Filter Chips Row
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Select Shift / Date",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            ),
                            color = TextDarkHeading
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            val shiftFilters = listOf("All Shifts", "Shift 1", "Shift 2", "Shift 3")
                            items(shiftFilters) { filter ->
                                val isSelected = selectedShiftFilter == filter
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { selectedShiftFilter = filter },
                                    label = {
                                        Text(
                                            text = filter,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                            )
                                        )
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = PrimaryBlue,
                                        selectedLabelColor = Color.White,
                                        containerColor = LightSurface,
                                        labelColor = TextDarkBody
                                    ),
                                    border = FilterChipDefaults.filterChipBorder(
                                        borderColor = if (isSelected) PrimaryBlue else BorderLight,
                                        selectedBorderColor = PrimaryBlue,
                                        enabled = true,
                                        selected = isSelected
                                    ),
                                    modifier = Modifier.testTag("pyq_shift_filter_${filter.lowercase().replace(" ", "_")}")
                                )
                            }
                        }
                    }
                }

                // Shift Papers List Placeholders
                items(filteredPapers, key = { it.id }) { shiftItem ->
                    PyqPaperShiftCard(shiftItem = shiftItem)
                }
            }
        }
    }
}

@Composable
private fun PyqPaperShiftCard(
    shiftItem: PyqPaperShiftItem,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LightSurface),
        border = BorderStroke(1.2.dp, BorderLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("pyq_shift_card_${shiftItem.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Row: Exam Date & Shift Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = shiftItem.examDate,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        ),
                        color = TextDarkHeading
                    )
                }

                // Shift Name Tag
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = LightCardBackgroundVariant,
                    border = BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.25f))
                ) {
                    Text(
                        text = "${shiftItem.shiftName} (${shiftItem.timeSlot})",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 10.sp,
                            color = PrimaryBlueDark
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Paper Title
            Text(
                text = shiftItem.paperTitle,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                ),
                color = TextDarkHeading
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 4 Grid Info Badges: Total Questions, Max Marks, Duration, Marking
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                PaperAttributeChip(
                    title = "Questions",
                    value = "${shiftItem.totalQuestions} Qs",
                    modifier = Modifier.weight(1f)
                )
                PaperAttributeChip(
                    title = "Max Marks",
                    value = "${shiftItem.maxMarks} M",
                    modifier = Modifier.weight(1f)
                )
                PaperAttributeChip(
                    title = "Duration",
                    value = "${shiftItem.durationMinutes} min",
                    modifier = Modifier.weight(1f)
                )
                PaperAttributeChip(
                    title = "Marking",
                    value = "${shiftItem.correctMarks} / ${shiftItem.negativeMarks}",
                    modifier = Modifier.weight(1.2f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = BorderLight)
            Spacer(modifier = Modifier.height(12.dp))

            // Bottom Action / Structure Indicator (Placeholder as specified)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircleOutline,
                        contentDescription = null,
                        tint = CorrectGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Official 4-Section Pattern (Bilingual)",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.5.sp,
                            color = TextMediumGray
                        )
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = LightCardBackgroundVariant,
                    border = BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.25f))
                ) {
                    Text(
                        text = "Paper Blueprint Ready",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.5.sp,
                            color = PrimaryBlueDark
                        ),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun MetricInfoPill(
    label: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = LightCardBackgroundVariant,
        border = BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.2f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PrimaryBlue,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 13.sp
                ),
                color = PrimaryBlueDark,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                ),
                color = TextMediumGray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun PaperAttributeChip(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = LightCardBackground,
        border = BorderStroke(1.dp, BorderLight),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                ),
                color = TextDarkHeading,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 9.sp,
                    color = TextMediumGray
                ),
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}
