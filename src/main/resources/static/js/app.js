/**
 * Smart Curriculum Activity & Attendance Portal - Client Side Script (Day 15)
 * Handles Student Dashboard Visual Metrics, Attendance Analytics, and API integration.
 */

document.addEventListener('DOMContentLoaded', () => {
    // Current active student state
    let activeStudent = {
        id: 1,
        rollNumber: '21CSE001',
        name: 'Sri Hariharan',
        department: 'Computer Science & Engineering',
        yearOfStudy: 3,
        status: 'ACTIVE'
    };

    let allAttendanceRecords = [];

    // Initialize Toast System
    const showToast = (message, type = 'info') => {
        const container = document.getElementById('toast-container');
        if (!container) return;

        const toast = document.createElement('div');
        toast.className = `toast toast-${type}`;
        const icons = { success: '✅', error: '❌', info: 'ℹ️' };
        toast.innerHTML = `<span>${icons[type] || 'ℹ️'}</span><span>${message}</span>`;
        container.appendChild(toast);

        setTimeout(() => {
            toast.style.opacity = '0';
            toast.style.transform = 'translateX(100%)';
            setTimeout(() => toast.remove(), 300);
        }, 4000);
    };

    // Initialize default date in quick form
    const quickDateInput = document.getElementById('quickDate');
    if (quickDateInput) {
        quickDateInput.value = new Date().toISOString().split('T')[0];
    }

    /**
     * Animate Radial Progress Meter
     */
    const updateRadialGauge = (percentage) => {
        const circle = document.getElementById('radialMeterCircle');
        const textElem = document.getElementById('radialPercentageText');
        const statusMsg = document.getElementById('attendanceStatusMsg');
        const overallTag = document.getElementById('overallEligibilityTag');

        const circumference = 2 * Math.PI * 68; // ≈ 427.25
        const validPct = Math.min(Math.max(percentage, 0), 100);
        const offset = circumference - (validPct / 100) * circumference;

        if (circle) {
            circle.style.strokeDasharray = `${circumference}`;
            circle.style.strokeDashoffset = `${offset}`;

            // Color coding according to institutional threshold
            if (validPct >= 75) {
                circle.style.stroke = '#10b981';
                circle.style.filter = 'drop-shadow(0 0 10px rgba(16, 185, 129, 0.45))';
                if (overallTag) {
                    overallTag.className = 'eligibility-tag tag-eligible';
                    overallTag.innerHTML = '✓ Exam Eligible (Good Standing)';
                }
                if (statusMsg) {
                    statusMsg.innerText = 'Above the mandatory 75% institutional threshold';
                    statusMsg.style.color = '#34d399';
                }
            } else if (validPct >= 65) {
                circle.style.stroke = '#f59e0b';
                circle.style.filter = 'drop-shadow(0 0 10px rgba(245, 158, 11, 0.45))';
                if (overallTag) {
                    overallTag.className = 'eligibility-tag tag-warning';
                    overallTag.innerHTML = '⚠️ Condonation Required (65-75%)';
                }
                if (statusMsg) {
                    statusMsg.innerText = 'Attendance between 65% and 75%. Institutional condonation needed.';
                    statusMsg.style.color = '#fbbf24';
                }
            } else {
                circle.style.stroke = '#f43f5e';
                circle.style.filter = 'drop-shadow(0 0 10px rgba(244, 63, 94, 0.45))';
                if (overallTag) {
                    overallTag.className = 'eligibility-tag tag-critical';
                    overallTag.innerHTML = '⛔ Shortage Alert (<65%)';
                }
                if (statusMsg) {
                    statusMsg.innerText = 'Critical shortage! Attendance is below 65%. Ineligible for exams.';
                    statusMsg.style.color = '#fb7185';
                }
            }
        }

        if (textElem) {
            textElem.innerText = `${validPct.toFixed(1)}%`;
        }
    };

    /**
     * Render Activity & Subject Attendance Breakdown
     */
    const renderActivityBreakdown = (metrics) => {
        const grid = document.getElementById('activityBreakdownGrid');
        if (!grid) return;

        if (!metrics || metrics.length === 0) {
            grid.innerHTML = `
                <div style="grid-column: 1 / -1; text-align: center; color: var(--text-muted); padding: 24px;">
                    No activity breakdown records available for this student.
                </div>
            `;
            return;
        }

        grid.innerHTML = metrics.map(item => {
            let colorClass = 'green';
            if (item.percentage < 65) colorClass = 'red';
            else if (item.percentage < 75) colorClass = 'amber';

            return `
                <div class="activity-bar-card">
                    <div class="activity-meta">
                        <div>
                            <div class="activity-name">${item.activityTitle || 'Curriculum Course'}</div>
                            <div style="font-size: 0.8rem; color: var(--text-dim); margin-top: 2px;">
                                ${item.activityType || 'THEORY'}
                            </div>
                        </div>
                        <span class="activity-code-badge">${item.activityCode || 'ACT'}</span>
                    </div>
                    <div class="activity-stats-row">
                        <span>Attended: <strong>${item.attendedSessions} / ${item.totalSessions}</strong></span>
                        <span class="activity-pct-text">${item.percentage.toFixed(1)}%</span>
                    </div>
                    <div class="progress-track">
                        <div class="progress-fill ${colorClass}" style="width: ${Math.min(item.percentage, 100)}%;"></div>
                    </div>
                </div>
            `;
        }).join('');
    };

    /**
     * Render Attendance Log History Table
     */
    const renderAttendanceTable = (records, filterStatus = 'ALL') => {
        const tbody = document.getElementById('attendanceTableBody');
        if (!tbody) return;

        let filtered = records;
        if (filterStatus !== 'ALL') {
            filtered = records.filter(r => (r.status || '').toUpperCase() === filterStatus.toUpperCase());
        }

        if (!filtered || filtered.length === 0) {
            tbody.innerHTML = `
                <tr>
                    <td colspan="5" style="text-align: center; color: var(--text-muted); padding: 24px;">
                        No attendance logs found matching the selected filter.
                    </td>
                </tr>
            `;
            return;
        }

        tbody.innerHTML = filtered.map(r => {
            let statusBadge = `<span class="badge-status badge-present">✓ Present</span>`;
            if (r.status === 'ABSENT') {
                statusBadge = `<span class="badge-status badge-absent">✗ Absent</span>`;
            } else if (r.status === 'ON_DUTY' || r.status === 'OD') {
                statusBadge = `<span class="badge-status badge-od">★ On-Duty</span>`;
            }

            return `
                <tr>
                    <td><strong>${r.attendanceDate || 'N/A'}</strong></td>
                    <td>
                        <div>${r.activityCode || 'N/A'}</div>
                    </td>
                    <td><span class="chip-type">${r.sessionSlot || 'SESSION_1'}</span></td>
                    <td>${statusBadge}</td>
                    <td style="color: var(--text-muted); font-size: 0.88rem;">${r.remarks || 'Regular Attendance'}</td>
                </tr>
            `;
        }).join('');
    };

    /**
     * Update Active Student UI Card
     */
    const updateStudentProfileUI = (student) => {
        if (!student) return;
        document.getElementById('headerRollNumber').innerText = student.rollNumber || 'N/A';
        document.getElementById('studentFullName').innerText = student.name || student.fullName || 'Student';
        document.getElementById('studentDept').innerText = student.department || 'Engineering';
        document.getElementById('studentYear').innerText = student.yearOfStudy ? `Year ${student.yearOfStudy}` : 'N/A';
        document.getElementById('studentStatus').innerText = student.status || 'ACTIVE';

        const quickId = document.getElementById('quickStudentId');
        if (quickId && student.id) {
            quickId.value = student.id;
        }
    };

    /**
     * Fetch and load student summary & metrics
     */
    const loadStudentDashboardMetrics = async (identifier, isRoll = false) => {
        try {
            const url = isRoll
                ? `/api/attendance/student/roll/${identifier}/summary`
                : `/api/attendance/student/${identifier}/summary`;

            const response = await fetch(url);
            if (response.ok) {
                const apiRes = await response.json();
                const data = apiRes.data;

                if (data) {
                    activeStudent = {
                        id: data.studentId,
                        rollNumber: data.studentRollNumber,
                        name: data.studentName,
                        department: data.department,
                        yearOfStudy: data.yearOfStudy,
                        status: 'ACTIVE'
                    };
                    updateStudentProfileUI(activeStudent);

                    // Update KPIs
                    document.getElementById('kpiOverallPct').innerText = `${data.attendancePercentage.toFixed(1)}%`;
                    document.getElementById('kpiTotalSessions').innerText = data.totalSessions;
                    document.getElementById('kpiAttendedSessions').innerText = data.attendedSessions;
                    document.getElementById('kpiAbsentSessions').innerText = data.absentSessions;

                    // Update Visual Gauge
                    updateRadialGauge(data.attendancePercentage);

                    // Update Activity Breakdown
                    renderActivityBreakdown(data.activityBreakdown || []);

                    // Update Attendance Log
                    allAttendanceRecords = data.recentRecords || [];
                    renderAttendanceTable(allAttendanceRecords);
                    return;
                }
            }
        } catch (err) {
            console.warn('API fetch warning, loading demonstration visual metrics:', err);
        }

        // Fallback demo metrics if database has no records yet
        applyDemoMetrics();
    };

    const applyDemoMetrics = () => {
        updateStudentProfileUI(activeStudent);
        updateRadialGauge(87.5);

        document.getElementById('kpiOverallPct').innerText = '87.5%';
        document.getElementById('kpiTotalSessions').innerText = '40';
        document.getElementById('kpiAttendedSessions').innerText = '35';
        document.getElementById('kpiAbsentSessions').innerText = '5';

        const demoActivities = [
            { activityCode: 'CS501-DS', activityTitle: 'Data Structures & Algorithms', activityType: 'THEORY & LAB', totalSessions: 16, attendedSessions: 15, percentage: 93.8 },
            { activityCode: 'CS502-DB', activityTitle: 'Database Management Systems', activityType: 'CORE COURSE', totalSessions: 12, attendedSessions: 10, percentage: 83.3 },
            { activityCode: 'ACT-AI-W01', activityTitle: 'Applied AI & ML Hands-on Workshop', activityType: 'WORKSHOP', totalSessions: 6, attendedSessions: 6, percentage: 100.0 },
            { activityCode: 'CS503-OS', activityTitle: 'Operating Systems & System Calls', activityType: 'LABORATORY', totalSessions: 6, attendedSessions: 4, percentage: 66.7 }
        ];
        renderActivityBreakdown(demoActivities);

        allAttendanceRecords = [
            { attendanceDate: '2026-09-24', activityCode: 'CS501-DS', sessionSlot: 'SESSION_1', status: 'PRESENT', remarks: 'Present in class' },
            { attendanceDate: '2026-09-23', activityCode: 'CS502-DB', sessionSlot: 'SESSION_2', status: 'PRESENT', remarks: 'Active lab participation' },
            { attendanceDate: '2026-09-22', activityCode: 'ACT-AI-W01', sessionSlot: 'SESSION_1', status: 'ON_DUTY', remarks: 'AI Symposium Attendance' },
            { attendanceDate: '2026-09-21', activityCode: 'CS503-OS', sessionSlot: 'SESSION_3', status: 'ABSENT', remarks: 'Sick leave' },
            { attendanceDate: '2026-09-20', activityCode: 'CS501-DS', sessionSlot: 'SESSION_1', status: 'PRESENT', remarks: 'Regular lecture' }
        ];
        renderAttendanceTable(allAttendanceRecords);
    };

    /**
     * Load Students into Dropdown
     */
    const loadStudentsDropdown = async () => {
        const dropdown = document.getElementById('studentSelectDropdown');
        if (!dropdown) return;

        try {
            const response = await fetch('/api/students');
            if (response.ok) {
                const apiRes = await response.json();
                const students = apiRes.data || [];
                if (students.length > 0) {
                    dropdown.innerHTML = students.map(s => 
                        `<option value="${s.id}" data-roll="${s.rollNumber}">${s.rollNumber} - ${s.name} (${s.department})</option>`
                    ).join('');

                    // Load the first student's metrics
                    activeStudent = students[0];
                    loadStudentDashboardMetrics(students[0].id, false);
                    return;
                }
            }
        } catch (e) {
            console.warn('Could not fetch student list:', e);
        }

        dropdown.innerHTML = `<option value="1" data-roll="21CSE001">21CSE001 - Sri Hariharan (CSE)</option>`;
        applyDemoMetrics();
    };

    // Dropdown change listener
    const dropdown = document.getElementById('studentSelectDropdown');
    if (dropdown) {
        dropdown.addEventListener('change', (e) => {
            const studentId = e.target.value;
            if (studentId) {
                loadStudentDashboardMetrics(studentId, false);
            }
        });
    }

    // Roll number search button
    const btnSearch = document.getElementById('btnSearchRoll');
    const searchInput = document.getElementById('rollNumberSearchInput');
    if (btnSearch && searchInput) {
        btnSearch.addEventListener('click', () => {
            const roll = searchInput.value.trim();
            if (!roll) {
                showToast('Please enter a roll number to search', 'error');
                return;
            }
            loadStudentDashboardMetrics(roll, true);
        });
    }

    // Refresh button
    const btnRefresh = document.getElementById('btnRefreshData');
    if (btnRefresh) {
        btnRefresh.addEventListener('click', () => {
            if (activeStudent.id) {
                loadStudentDashboardMetrics(activeStudent.id, false);
                showToast('Dashboard metrics refreshed', 'info');
            }
        });
    }

    // Attendance status filter
    const statusFilter = document.getElementById('attendanceStatusFilter');
    if (statusFilter) {
        statusFilter.addEventListener('change', (e) => {
            renderAttendanceTable(allAttendanceRecords, e.target.value);
        });
    }

    // Form: Mark Attendance Quick Form
    const markForm = document.getElementById('markAttendanceQuickForm');
    if (markForm) {
        markForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const formData = new FormData(markForm);
            const payload = {
                studentId: Number(formData.get('studentId')),
                activityId: Number(formData.get('activityId')),
                attendanceDate: formData.get('attendanceDate'),
                status: formData.get('status'),
                sessionSlot: formData.get('sessionSlot') || 'SESSION_1',
                remarks: 'Marked from Student Dashboard'
            };

            try {
                const response = await fetch('/api/attendance', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(payload)
                });
                const result = await response.json();
                if (response.ok) {
                    showToast('Attendance logged successfully!', 'success');
                    loadStudentDashboardMetrics(payload.studentId, false);
                } else {
                    showToast(result.message || 'Failed to mark attendance', 'error');
                }
            } catch (err) {
                showToast('Network error: ' + err.message, 'error');
            }
        });
    }

    // Form: Create Student
    const createForm = document.getElementById('createStudentForm');
    if (createForm) {
        createForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const data = Object.fromEntries(new FormData(createForm).entries());
            data.yearOfStudy = Number(data.yearOfStudy);

            try {
                const response = await fetch('/api/students', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(data)
                });
                const result = await response.json();
                if (response.ok) {
                    showToast('Student created successfully!', 'success');
                    createForm.reset();
                    loadStudentsDropdown();
                } else {
                    showToast(result.message || 'Failed to create student', 'error');
                }
            } catch (err) {
                showToast('Network error: ' + err.message, 'error');
            }
        });
    }

    // Initial Load
    loadStudentsDropdown();
});
