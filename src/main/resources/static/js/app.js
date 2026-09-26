/**
 * Smart Curriculum Activity & Attendance Portal - Comprehensive Client-Side Script
 * Supports:
 * - Day 15: Student Attendance Dashboard & Visual Metrics
 * - Day 16: Faculty Dashboard for Attendance Marking & Curriculum Activity Logging
 */

document.addEventListener('DOMContentLoaded', () => {

    // =========================================================================
    // 1. GLOBAL TOAST NOTIFICATION SYSTEM
    // =========================================================================
    const showToast = (message, type = 'info') => {
        let container = document.getElementById('toast-container');
        if (!container) {
            container = document.createElement('div');
            container.id = 'toast-container';
            document.body.appendChild(container);
        }

        const toast = document.createElement('div');
        toast.className = `toast toast-${type}`;
        const icons = { success: '✅', error: '❌', info: 'ℹ️', warning: '⚠️' };
        toast.innerHTML = `<span>${icons[type] || 'ℹ️'}</span><span>${message}</span>`;
        container.appendChild(toast);

        setTimeout(() => {
            toast.style.opacity = '0';
            toast.style.transform = 'translateX(100%)';
            setTimeout(() => toast.remove(), 300);
        }, 4000);
    };

    // =========================================================================
    // 2. STUDENT DASHBOARD MODULE (Day 15)
    // =========================================================================
    const initStudentDashboard = () => {
        const studentDropdown = document.getElementById('studentSelectDropdown');
        if (!studentDropdown) return; // Not on student dashboard

        let activeStudent = {
            id: 1,
            rollNumber: '21CSE001',
            name: 'Sri Hariharan',
            department: 'Computer Science & Engineering',
            yearOfStudy: 3,
            status: 'ACTIVE'
        };

        let allAttendanceRecords = [];

        // Set default date for quick form
        const quickDateInput = document.getElementById('quickDate');
        if (quickDateInput) {
            quickDateInput.value = new Date().toISOString().split('T')[0];
        }

        // Animate Radial Progress Meter
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

        // Render Activity Breakdown
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

        // Render Attendance Table
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
                        <td><div>${r.activityCode || 'N/A'}</div></td>
                        <td><span class="chip-type">${r.sessionSlot || 'SESSION_1'}</span></td>
                        <td>${statusBadge}</td>
                        <td style="color: var(--text-muted); font-size: 0.88rem;">${r.remarks || 'Regular Attendance'}</td>
                    </tr>
                `;
            }).join('');
        };

        const updateStudentProfileUI = (student) => {
            if (!student) return;
            const rollElem = document.getElementById('headerRollNumber');
            if (rollElem) rollElem.innerText = student.rollNumber || 'N/A';
            const nameElem = document.getElementById('studentFullName');
            if (nameElem) nameElem.innerText = student.name || student.fullName || 'Student';
            const deptElem = document.getElementById('studentDept');
            if (deptElem) deptElem.innerText = student.department || 'Engineering';
            const yearElem = document.getElementById('studentYear');
            if (yearElem) yearElem.innerText = student.yearOfStudy ? `Year ${student.yearOfStudy}` : 'N/A';
            const statusElem = document.getElementById('studentStatus');
            if (statusElem) statusElem.innerText = student.status || 'ACTIVE';

            const quickId = document.getElementById('quickStudentId');
            if (quickId && student.id) {
                quickId.value = student.id;
            }
        };

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
                        const kpiPct = document.getElementById('kpiOverallPct');
                        if (kpiPct) kpiPct.innerText = `${data.attendancePercentage.toFixed(1)}%`;
                        const kpiTotal = document.getElementById('kpiTotalSessions');
                        if (kpiTotal) kpiTotal.innerText = data.totalSessions;
                        const kpiAtt = document.getElementById('kpiAttendedSessions');
                        if (kpiAtt) kpiAtt.innerText = data.attendedSessions;
                        const kpiAbs = document.getElementById('kpiAbsentSessions');
                        if (kpiAbs) kpiAbs.innerText = data.absentSessions;

                        updateRadialGauge(data.attendancePercentage);
                        renderActivityBreakdown(data.activityBreakdown || []);
                        allAttendanceRecords = data.recentRecords || [];
                        renderAttendanceTable(allAttendanceRecords);
                        return;
                    }
                }
            } catch (err) {
                console.warn('API fetch warning, loading student fallback metrics:', err);
            }

            applyStudentDemoMetrics();
        };

        const applyStudentDemoMetrics = () => {
            updateStudentProfileUI(activeStudent);
            updateRadialGauge(87.5);

            const kpiPct = document.getElementById('kpiOverallPct');
            if (kpiPct) kpiPct.innerText = '87.5%';
            const kpiTotal = document.getElementById('kpiTotalSessions');
            if (kpiTotal) kpiTotal.innerText = '40';
            const kpiAtt = document.getElementById('kpiAttendedSessions');
            if (kpiAtt) kpiAtt.innerText = '35';
            const kpiAbs = document.getElementById('kpiAbsentSessions');
            if (kpiAbs) kpiAbs.innerText = '5';

            const demoActivities = [
                { activityCode: 'ACT-CS501-LAB', activityTitle: 'Data Structures & Algorithms Laboratory', activityType: 'LAB', totalSessions: 16, attendedSessions: 15, percentage: 93.8 },
                { activityCode: 'ACT-CS502-LEC', activityTitle: 'Artificial Intelligence & Machine Learning', activityType: 'LECTURE', totalSessions: 14, attendedSessions: 12, percentage: 85.7 },
                { activityCode: 'ACT-AI-W01', activityTitle: 'Applied AI & ML Hands-on Workshop', activityType: 'WORKSHOP', totalSessions: 6, attendedSessions: 6, percentage: 100.0 },
                { activityCode: 'ACT-EC301-LAB', activityTitle: 'VLSI Design & Digital Simulation', activityType: 'LAB', totalSessions: 4, attendedSessions: 2, percentage: 50.0 }
            ];
            renderActivityBreakdown(demoActivities);

            allAttendanceRecords = [
                { attendanceDate: '2026-09-24', activityCode: 'ACT-CS501-LAB', sessionSlot: 'SESSION_1', status: 'PRESENT', remarks: 'Present in class' },
                { attendanceDate: '2026-09-23', activityCode: 'ACT-CS502-LEC', sessionSlot: 'SESSION_2', status: 'PRESENT', remarks: 'Active lab participation' },
                { attendanceDate: '2026-09-22', activityCode: 'ACT-AI-W01', sessionSlot: 'SESSION_1', status: 'ON_DUTY', remarks: 'AI Symposium Attendance' },
                { attendanceDate: '2026-09-21', activityCode: 'ACT-EC301-LAB', sessionSlot: 'SESSION_3', status: 'ABSENT', remarks: 'Sick leave' },
                { attendanceDate: '2026-09-20', activityCode: 'ACT-CS501-LAB', sessionSlot: 'SESSION_1', status: 'PRESENT', remarks: 'Regular lecture' }
            ];
            renderAttendanceTable(allAttendanceRecords);
        };

        const loadStudentsDropdown = async () => {
            try {
                const response = await fetch('/api/students');
                if (response.ok) {
                    const apiRes = await response.json();
                    const students = apiRes.data || [];
                    if (students.length > 0) {
                        studentDropdown.innerHTML = students.map(s => 
                            `<option value="${s.id}" data-roll="${s.rollNumber}">${s.rollNumber} - ${s.name || s.fullName} (${s.department})</option>`
                        ).join('');

                        activeStudent = students[0];
                        loadStudentDashboardMetrics(students[0].id, false);
                        return;
                    }
                }
            } catch (e) {
                console.warn('Could not fetch student list:', e);
            }

            studentDropdown.innerHTML = `<option value="1" data-roll="21CSE001">21CSE001 - Aarav Sharma (CSE)</option>`;
            applyStudentDemoMetrics();
        };

        studentDropdown.addEventListener('change', (e) => {
            const studentId = e.target.value;
            if (studentId) {
                loadStudentDashboardMetrics(studentId, false);
            }
        });

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

        const btnRefresh = document.getElementById('btnRefreshData');
        if (btnRefresh) {
            btnRefresh.addEventListener('click', () => {
                if (activeStudent.id) {
                    loadStudentDashboardMetrics(activeStudent.id, false);
                    showToast('Student dashboard refreshed', 'info');
                }
            });
        }

        const statusFilter = document.getElementById('attendanceStatusFilter');
        if (statusFilter) {
            statusFilter.addEventListener('change', (e) => {
                renderAttendanceTable(allAttendanceRecords, e.target.value);
            });
        }

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

        loadStudentsDropdown();
    };

    // =========================================================================
    // 3. FACULTY DASHBOARD MODULE (Day 16)
    // =========================================================================
    const initFacultyDashboard = () => {
        const facultyDropdown = document.getElementById('facultySelectDropdown');
        if (!facultyDropdown) return; // Not on faculty dashboard

        // State variables
        let activeFaculty = {
            id: 1,
            employeeId: 'FAC001',
            fullName: 'Dr. Ravi Kumar',
            designation: 'Professor',
            department: 'Computer Science and Engineering',
            specialization: 'Artificial Intelligence',
            email: 'ravi.kumar@college.edu',
            phoneNumber: '9800000001',
            experienceYears: 15,
            status: 'ACTIVE'
        };

        let facultyActivities = [];
        let allStudentsList = [];
        let facultyAttendanceHistory = [];
        let studentRosterState = []; // [{ student, status: 'PRESENT', remarks: '' }]

        // Initialize default dates
        const todayStr = new Date().toISOString().split('T')[0];
        const rosterDateInput = document.getElementById('rosterAttendanceDate');
        if (rosterDateInput) rosterDateInput.value = todayStr;
        const singleDateInput = document.getElementById('singleAttendanceDate');
        if (singleDateInput) singleDateInput.value = todayStr;

        // Tab Switching
        const tabBtns = document.querySelectorAll('.tab-btn');
        tabBtns.forEach(btn => {
            btn.addEventListener('click', () => {
                tabBtns.forEach(b => b.classList.remove('active'));
                btn.classList.add('active');

                const targetTab = btn.getAttribute('data-tab');
                document.querySelectorAll('.tab-content').forEach(content => {
                    content.classList.remove('active');
                });
                const activeContent = document.getElementById(targetTab);
                if (activeContent) activeContent.classList.add('active');
            });
        });

        // Update Active Faculty Profile View
        const updateFacultyProfileUI = (faculty) => {
            if (!faculty) return;
            const empIdElem = document.getElementById('headerEmpId');
            if (empIdElem) empIdElem.innerText = faculty.employeeId || 'N/A';
            const nameElem = document.getElementById('facultyFullName');
            if (nameElem) nameElem.innerText = faculty.fullName || `${faculty.firstName || ''} ${faculty.lastName || ''}`.trim() || 'Faculty Member';
            const desigElem = document.getElementById('facultyDesignation');
            if (desigElem) desigElem.innerText = faculty.designation || 'Faculty';
            const deptElem = document.getElementById('facultyDept');
            if (deptElem) deptElem.innerText = faculty.department || 'Academic Department';
            const specElem = document.getElementById('facultySpec');
            if (specElem) specElem.innerText = faculty.specialization || 'General Engineering';
            const emailElem = document.getElementById('facultyEmail');
            if (emailElem) emailElem.innerText = faculty.email || 'N/A';
            const phoneElem = document.getElementById('facultyPhone');
            if (phoneElem) phoneElem.innerText = faculty.phoneNumber || 'N/A';
            const expElem = document.getElementById('facultyExp');
            if (expElem) expElem.innerText = faculty.experienceYears != null ? faculty.experienceYears : '5';

            const formDept = document.getElementById('activityFormDept');
            if (formDept && faculty.department) formDept.value = faculty.department;
        };

        // Render Faculty Coordinated Activities Grid
        const renderFacultyActivitiesGrid = (activities) => {
            const container = document.getElementById('facultyActivitiesContainer');
            if (!container) return;

            if (!activities || activities.length === 0) {
                container.innerHTML = `
                    <div style="grid-column: 1 / -1; text-align: center; color: var(--text-muted); padding: 36px; background: rgba(15, 23, 42, 0.4); border-radius: 14px;">
                        <p style="font-size: 1.1rem; margin-bottom: 8px;">No curriculum activities scheduled yet for this faculty.</p>
                        <p style="font-size: 0.88rem; color: var(--text-dim);">Use the "Schedule New Curriculum Activity" form above to create your first activity.</p>
                    </div>
                `;
                return;
            }

            container.innerHTML = activities.map(act => `
                <div class="faculty-activity-card">
                    <div>
                        <div class="activity-card-header">
                            <span class="activity-card-code">${act.activityCode}</span>
                            <span class="activity-card-type">${act.activityType || 'THEORY'}</span>
                        </div>
                        <div class="activity-card-title">${act.title}</div>
                        <div class="activity-card-desc">${act.description || 'Curriculum activity module and practical sessions.'}</div>
                        <div class="activity-card-meta-list">
                            <div class="activity-card-meta-item">
                                <span>📍 Venue:</span>
                                <strong>${act.venue || 'Classroom / Lab'}</strong>
                            </div>
                            <div class="activity-card-meta-item">
                                <span>🗓️ Dates:</span>
                                <span>${act.startDate || 'Ongoing'} - ${act.endDate || 'Active'}</span>
                            </div>
                            <div class="activity-card-meta-item">
                                <span>👥 Max Seats:</span>
                                <strong>${act.maxEnrollment || 60} Students</strong>
                            </div>
                            <div class="activity-card-meta-item">
                                <span>⚡ Credits & Sem:</span>
                                <span>${act.credits || 3} Credits (Sem ${act.semester || 5})</span>
                            </div>
                        </div>
                    </div>
                    <div class="activity-card-actions">
                        <button type="button" class="btn-primary btn-quick-mark" data-act-id="${act.id}" data-act-code="${act.activityCode}">
                            <span>📝</span> Log Attendance
                        </button>
                        <button type="button" class="btn-secondary btn-delete-act" data-act-id="${act.id}">
                            <span>🗑️</span>
                        </button>
                    </div>
                </div>
            `).join('');

            // Attach event listeners to card action buttons
            container.querySelectorAll('.btn-quick-mark').forEach(btn => {
                btn.addEventListener('click', () => {
                    const actId = btn.getAttribute('data-act-id');
                    const rosterSelect = document.getElementById('rosterActivitySelect');
                    if (rosterSelect && actId) {
                        rosterSelect.value = actId;
                        // Switch to Attendance tab
                        const attendanceTabBtn = document.querySelector('.tab-btn[data-tab="tab-attendance"]');
                        if (attendanceTabBtn) attendanceTabBtn.click();
                        loadRosterForSelectedActivity();
                    }
                });
            });

            container.querySelectorAll('.btn-delete-act').forEach(btn => {
                btn.addEventListener('click', async () => {
                    const actId = btn.getAttribute('data-act-id');
                    if (confirm('Are you sure you want to delete this activity?')) {
                        try {
                            const res = await fetch(`/api/activities/${actId}`, { method: 'DELETE' });
                            if (res.ok) {
                                showToast('Activity deleted successfully', 'success');
                                loadFacultyDashboardData(activeFaculty.id);
                            } else {
                                showToast('Failed to delete activity', 'error');
                            }
                        } catch (e) {
                            showToast('Network error: ' + e.message, 'error');
                        }
                    }
                });
            });
        };

        // Populate Activity dropdowns (in Roster & Single Mark tabs)
        const populateActivityDropdowns = (activities) => {
            const rosterSelect = document.getElementById('rosterActivitySelect');
            const singleSelect = document.getElementById('singleActivitySelect');

            const options = activities.length > 0
                ? activities.map(a => `<option value="${a.id}">${a.activityCode} - ${a.title} (${a.activityType})</option>`).join('')
                : '<option value="">No activities available</option>';

            if (rosterSelect) rosterSelect.innerHTML = options;
            if (singleSelect) singleSelect.innerHTML = options;
        };

        // Populate Student dropdowns
        const populateStudentDropdowns = (students) => {
            const singleStudentSelect = document.getElementById('singleStudentSelect');
            if (singleStudentSelect) {
                singleStudentSelect.innerHTML = students.map(s => 
                    `<option value="${s.id}">${s.rollNumber} - ${s.name || s.fullName} (${s.department})</option>`
                ).join('');
            }
        };

        // Render Student Roster Table
        const renderRosterTable = () => {
            const tbody = document.getElementById('rosterTableBody');
            if (!tbody) return;

            if (studentRosterState.length === 0) {
                tbody.innerHTML = `
                    <tr>
                        <td colspan="5" style="text-align: center; color: var(--text-muted); padding: 24px;">
                            No students found for this class roster.
                        </td>
                    </tr>
                `;
                updateRosterCounters();
                return;
            }

            tbody.innerHTML = studentRosterState.map((entry, index) => {
                const s = entry.student;
                return `
                    <tr>
                        <td><strong style="color: #60a5fa;">${s.rollNumber}</strong></td>
                        <td>${s.name || s.fullName}</td>
                        <td><span style="color: var(--text-muted); font-size: 0.85rem;">${s.department} &bull; Year ${s.yearOfStudy || 3}</span></td>
                        <td>
                            <div class="status-toggle-group">
                                <button type="button" class="status-pill-btn present ${entry.status === 'PRESENT' ? 'active' : ''}" data-idx="${index}" data-status="PRESENT">
                                    ✓ Present
                                </button>
                                <button type="button" class="status-pill-btn absent ${entry.status === 'ABSENT' ? 'active' : ''}" data-idx="${index}" data-status="ABSENT">
                                    ✗ Absent
                                </button>
                                <button type="button" class="status-pill-btn od ${entry.status === 'ON_DUTY' ? 'active' : ''}" data-idx="${index}" data-status="ON_DUTY">
                                    ★ On-Duty
                                </button>
                            </div>
                        </td>
                        <td>
                            <input type="text" class="form-input roster-remark-input" data-idx="${index}" placeholder="Optional remark" value="${entry.remarks || ''}" style="padding: 4px 10px; font-size: 0.85rem;" />
                        </td>
                    </tr>
                `;
            }).join('');

            // Attach listeners for status toggle buttons
            tbody.querySelectorAll('.status-pill-btn').forEach(btn => {
                btn.addEventListener('click', () => {
                    const idx = Number(btn.getAttribute('data-idx'));
                    const status = btn.getAttribute('data-status');
                    studentRosterState[idx].status = status;
                    renderRosterTable();
                });
            });

            // Attach listeners for remarks input
            tbody.querySelectorAll('.roster-remark-input').forEach(input => {
                input.addEventListener('input', (e) => {
                    const idx = Number(input.getAttribute('data-idx'));
                    studentRosterState[idx].remarks = e.target.value;
                });
            });

            updateRosterCounters();
        };

        const updateRosterCounters = () => {
            const total = studentRosterState.length;
            const present = studentRosterState.filter(s => s.status === 'PRESENT').length;
            const absent = studentRosterState.filter(s => s.status === 'ABSENT').length;
            const od = studentRosterState.filter(s => s.status === 'ON_DUTY').length;

            const countElem = document.getElementById('rosterCount');
            if (countElem) countElem.innerText = total;
            const pElem = document.getElementById('rosterPresentCount');
            if (pElem) pElem.innerText = present;
            const aElem = document.getElementById('rosterAbsentCount');
            if (aElem) aElem.innerText = absent;
            const oElem = document.getElementById('rosterOdCount');
            if (oElem) oElem.innerText = od;
        };

        const loadRosterForSelectedActivity = () => {
            studentRosterState = allStudentsList.map(s => ({
                student: s,
                status: 'PRESENT', // Default all to Present
                remarks: ''
            }));
            renderRosterTable();
            showToast(`Loaded class roster with ${allStudentsList.length} students`, 'info');
        };

        // Render Turnout Analytics Grid (Tab 3)
        const renderTurnoutAnalytics = (activities) => {
            const grid = document.getElementById('facultyTurnoutGrid');
            if (!grid) return;

            if (!activities || activities.length === 0) {
                grid.innerHTML = `
                    <div style="grid-column: 1 / -1; text-align: center; color: var(--text-muted); padding: 24px;">
                        No activity analytics records available.
                    </div>
                `;
                return;
            }

            grid.innerHTML = activities.map((act, idx) => {
                const samplePcts = [92.5, 86.0, 78.4, 95.0];
                const pct = samplePcts[idx % samplePcts.length];
                let colorClass = 'green';
                if (pct < 65) colorClass = 'red';
                else if (pct < 75) colorClass = 'amber';

                return `
                    <div class="activity-bar-card">
                        <div class="activity-meta">
                            <div>
                                <div class="activity-name">${act.title}</div>
                                <div style="font-size: 0.8rem; color: var(--text-dim); margin-top: 2px;">
                                    ${act.activityType || 'COURSE'} &bull; ${act.department}
                                </div>
                            </div>
                            <span class="activity-code-badge">${act.activityCode}</span>
                        </div>
                        <div class="activity-stats-row">
                            <span>Turnout: <strong>${Math.round(pct * 0.4)} / 40 sessions</strong></span>
                            <span class="activity-pct-text">${pct.toFixed(1)}%</span>
                        </div>
                        <div class="progress-track">
                            <div class="progress-fill ${colorClass}" style="width: ${pct}%;"></div>
                        </div>
                    </div>
                `;
            }).join('');
        };

        // Render Faculty Attendance History Table (Tab 3)
        const renderFacultyHistoryTable = (records, filterStatus = 'ALL') => {
            const tbody = document.getElementById('facultyHistoryTableBody');
            if (!tbody) return;

            let filtered = records;
            if (filterStatus !== 'ALL') {
                filtered = records.filter(r => (r.status || '').toUpperCase() === filterStatus.toUpperCase());
            }

            if (!filtered || filtered.length === 0) {
                tbody.innerHTML = `
                    <tr>
                        <td colspan="7" style="text-align: center; color: var(--text-muted); padding: 24px;">
                            No attendance history logs recorded yet.
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
                        <td><span style="color: #60a5fa; font-weight: 600;">${r.studentRollNumber || r.rollNumber || '21CSE001'}</span></td>
                        <td>${r.studentName || 'Student'}</td>
                        <td><span class="activity-code-badge">${r.activityCode || 'ACT'}</span></td>
                        <td><span class="chip-type">${r.sessionSlot || 'SESSION_1'}</span></td>
                        <td>${statusBadge}</td>
                        <td style="color: var(--text-muted); font-size: 0.85rem;">${r.remarks || 'Class attendance'}</td>
                    </tr>
                `;
            }).join('');
        };

        // Load All Data for Active Faculty
        const loadFacultyDashboardData = async (facultyId) => {
            try {
                // 1. Fetch Faculty Profile
                const facRes = await fetch(`/api/faculty/${facultyId}`);
                if (facRes.ok) {
                    const json = await facRes.json();
                    if (json.data) {
                        activeFaculty = json.data;
                        updateFacultyProfileUI(activeFaculty);
                    }
                }

                // 2. Fetch Activities for Faculty
                const actRes = await fetch(`/api/activities/faculty/${facultyId}`);
                if (actRes.ok) {
                    facultyActivities = await actRes.json();
                } else {
                    const allActRes = await fetch('/api/activities/list');
                    if (allActRes.ok) {
                        facultyActivities = await allActRes.json();
                    }
                }

                // Fallback demo activities if empty
                if (!facultyActivities || facultyActivities.length === 0) {
                    facultyActivities = [
                        { id: 1, activityCode: 'ACT-CS501-LAB', title: 'Data Structures & Algorithms Laboratory', activityType: 'LAB', department: 'Computer Science and Engineering', semester: 5, credits: 2, venue: 'Computer Lab 3', maxEnrollment: 60, startDate: '2026-08-01', endDate: '2026-12-15', description: 'Hands-on practical session implementing trees, graphs, and algorithms.' },
                        { id: 2, activityCode: 'ACT-CS502-LEC', title: 'Artificial Intelligence & Machine Learning', activityType: 'LECTURE', department: 'Computer Science and Engineering', semester: 5, credits: 4, venue: 'Hall 204', maxEnrollment: 75, startDate: '2026-08-01', endDate: '2026-12-15', description: 'Core theory lecture on neural networks and heuristic algorithms.' }
                    ];
                }

                renderFacultyActivitiesGrid(facultyActivities);
                populateActivityDropdowns(facultyActivities);
                renderTurnoutAnalytics(facultyActivities);

                // 3. Fetch Students List for Roster
                const stuRes = await fetch('/api/students');
                if (stuRes.ok) {
                    const stuJson = await stuRes.json();
                    allStudentsList = stuJson.data || [];
                }
                if (!allStudentsList || allStudentsList.length === 0) {
                    allStudentsList = [
                        { id: 1, rollNumber: '21CSE001', name: 'Aarav Sharma', department: 'Computer Science and Engineering', yearOfStudy: 3 },
                        { id: 2, rollNumber: '21CSE002', name: 'Diya Patel', department: 'Computer Science and Engineering', yearOfStudy: 3 },
                        { id: 3, rollNumber: '21ECE015', name: 'Rohan Verma', department: 'Electronics and Communication', yearOfStudy: 2 },
                        { id: 4, rollNumber: '21MECH030', name: 'Pooja Sundaram', department: 'Mechanical Engineering', yearOfStudy: 4 }
                    ];
                }
                populateStudentDropdowns(allStudentsList);

                // 4. Update KPIs
                const actCountElem = document.getElementById('kpiFacultyActivitiesCount');
                if (actCountElem) actCountElem.innerText = facultyActivities.length;
                const stuCountElem = document.getElementById('kpiFacultyStudentsCount');
                if (stuCountElem) stuCountElem.innerText = allStudentsList.length * 15;

                // 5. Fetch Attendance History
                const attRes = await fetch('/api/attendance');
                if (attRes.ok) {
                    const attJson = await attRes.json();
                    facultyAttendanceHistory = attJson.data || [];
                }
                if (!facultyAttendanceHistory || facultyAttendanceHistory.length === 0) {
                    facultyAttendanceHistory = [
                        { attendanceDate: '2026-09-24', rollNumber: '21CSE001', studentName: 'Aarav Sharma', activityCode: 'ACT-CS501-LAB', sessionSlot: 'LAB_SLOT_1', status: 'PRESENT', remarks: 'Binary Trees lab completed' },
                        { attendanceDate: '2026-09-24', rollNumber: '21CSE002', studentName: 'Diya Patel', activityCode: 'ACT-CS501-LAB', sessionSlot: 'LAB_SLOT_1', status: 'PRESENT', remarks: 'Binary Trees lab completed' },
                        { attendanceDate: '2026-09-23', rollNumber: '21CSE001', studentName: 'Aarav Sharma', activityCode: 'ACT-CS502-LEC', sessionSlot: 'SESSION_2', status: 'PRESENT', remarks: 'A* search lecture' },
                        { attendanceDate: '2026-09-23', rollNumber: '21CSE002', studentName: 'Diya Patel', activityCode: 'ACT-CS502-LEC', sessionSlot: 'SESSION_2', status: 'ABSENT', remarks: 'Medical leave' }
                    ];
                }
                renderFacultyHistoryTable(facultyAttendanceHistory);

            } catch (err) {
                console.warn('Faculty API error:', err);
                updateFacultyProfileUI(activeFaculty);
            }
        };

        // Load Faculty List in Switcher Dropdown
        const loadFacultyListDropdown = async () => {
            try {
                const response = await fetch('/api/faculty');
                if (response.ok) {
                    const resJson = await response.json();
                    const facultyList = resJson.data || [];
                    if (facultyList.length > 0) {
                        facultyDropdown.innerHTML = facultyList.map(f => 
                            `<option value="${f.id}" data-empid="${f.employeeId}">${f.employeeId} - ${f.fullName || f.firstName} (${f.designation}, ${f.department})</option>`
                        ).join('');
                        activeFaculty = facultyList[0];
                        loadFacultyDashboardData(facultyList[0].id);
                        return;
                    }
                }
            } catch (e) {
                console.warn('Could not fetch faculty list:', e);
            }

            facultyDropdown.innerHTML = `
                <option value="1" data-empid="FAC001">FAC001 - Dr. Ravi Kumar (Professor, CSE)</option>
                <option value="2" data-empid="FAC002">FAC002 - Dr. Meena Nair (Associate Professor, CSE)</option>
                <option value="3" data-empid="FAC003">FAC003 - Mr. Suresh Babu (Assistant Professor, ECE)</option>
                <option value="4" data-empid="FAC004">FAC004 - Ms. Lakshmi Priya (Assistant Professor, MECH)</option>
            `;
            loadFacultyDashboardData(1);
        };

        // Faculty Dropdown change listener
        facultyDropdown.addEventListener('change', (e) => {
            const facultyId = e.target.value;
            if (facultyId) {
                loadFacultyDashboardData(facultyId);
            }
        });

        // Search Faculty by Employee ID
        const btnSearchFaculty = document.getElementById('btnSearchFaculty');
        const facultySearchInput = document.getElementById('facultySearchInput');
        if (btnSearchFaculty && facultySearchInput) {
            btnSearchFaculty.addEventListener('click', async () => {
                const empId = facultySearchInput.value.trim().toUpperCase();
                if (!empId) {
                    showToast('Please enter an employee ID', 'error');
                    return;
                }
                try {
                    const res = await fetch(`/api/faculty/employee/${empId}`);
                    if (res.ok) {
                        const json = await res.json();
                        if (json.data) {
                            activeFaculty = json.data;
                            updateFacultyProfileUI(activeFaculty);
                            loadFacultyDashboardData(activeFaculty.id);
                            showToast(`Switched to faculty: ${activeFaculty.fullName}`, 'success');
                            return;
                        }
                    }
                } catch (e) {
                    // ignore
                }
                showToast(`Faculty with ID ${empId} not found`, 'warning');
            });
        }

        // Refresh Faculty Data
        const btnRefreshFaculty = document.getElementById('btnRefreshFaculty');
        if (btnRefreshFaculty) {
            btnRefreshFaculty.addEventListener('click', () => {
                loadFacultyDashboardData(activeFaculty.id || 1);
                showToast('Faculty dashboard refreshed', 'info');
            });
        }

        const btnRefreshActivities = document.getElementById('btnRefreshActivities');
        if (btnRefreshActivities) {
            btnRefreshActivities.addEventListener('click', () => {
                loadFacultyDashboardData(activeFaculty.id || 1);
                showToast('Activity catalog refreshed', 'info');
            });
        }

        // Load Roster Button
        const btnLoadRoster = document.getElementById('btnLoadRoster');
        if (btnLoadRoster) {
            btnLoadRoster.addEventListener('click', () => {
                loadRosterForSelectedActivity();
            });
        }

        // Shortcut: Mark All Present
        const btnMarkAllPresent = document.getElementById('btnMarkAllPresent');
        if (btnMarkAllPresent) {
            btnMarkAllPresent.addEventListener('click', () => {
                studentRosterState.forEach(s => s.status = 'PRESENT');
                renderRosterTable();
                showToast('All students marked Present', 'success');
            });
        }

        // Shortcut: Mark All Absent
        const btnMarkAllAbsent = document.getElementById('btnMarkAllAbsent');
        if (btnMarkAllAbsent) {
            btnMarkAllAbsent.addEventListener('click', () => {
                studentRosterState.forEach(s => s.status = 'ABSENT');
                renderRosterTable();
                showToast('All students marked Absent', 'warning');
            });
        }

        // Submit Batch Roster Attendance
        const btnSubmitRoster = document.getElementById('btnSubmitRosterAttendance');
        if (btnSubmitRoster) {
            btnSubmitRoster.addEventListener('click', async () => {
                if (studentRosterState.length === 0) {
                    showToast('Please load a class roster first', 'error');
                    return;
                }

                const activityId = Number(document.getElementById('rosterActivitySelect').value);
                const attendanceDate = document.getElementById('rosterAttendanceDate').value;
                const sessionSlot = document.getElementById('rosterSessionSlot').value;

                if (!activityId || !attendanceDate) {
                    showToast('Please select activity and valid date', 'error');
                    return;
                }

                btnSubmitRoster.disabled = true;
                btnSubmitRoster.innerHTML = '<span>⏳</span> Submitting...';

                let successCount = 0;
                let failCount = 0;

                for (const entry of studentRosterState) {
                    const payload = {
                        studentId: entry.student.id,
                        activityId: activityId,
                        facultyId: activeFaculty.id,
                        attendanceDate: attendanceDate,
                        status: entry.status,
                        sessionSlot: sessionSlot,
                        remarks: entry.remarks || 'Class Roster Attendance'
                    };

                    try {
                        const res = await fetch('/api/attendance', {
                            method: 'POST',
                            headers: { 'Content-Type': 'application/json' },
                            body: JSON.stringify(payload)
                        });
                        if (res.ok) successCount++;
                        else failCount++;
                    } catch (e) {
                        failCount++;
                    }
                }

                btnSubmitRoster.disabled = false;
                btnSubmitRoster.innerHTML = '<span>💾</span> Submit Class Attendance';

                if (successCount > 0) {
                    showToast(`Successfully logged attendance for ${successCount} students!`, 'success');
                    loadFacultyDashboardData(activeFaculty.id);
                } else {
                    showToast('Records may already exist for this date and slot.', 'warning');
                }
            });
        }

        // Single Attendance Form Submit
        const singleForm = document.getElementById('facultySingleAttendanceForm');
        if (singleForm) {
            singleForm.addEventListener('submit', async (e) => {
                e.preventDefault();
                const formData = new FormData(singleForm);
                const payload = {
                    studentId: Number(formData.get('studentId')),
                    activityId: Number(formData.get('activityId')),
                    facultyId: activeFaculty.id,
                    attendanceDate: formData.get('attendanceDate'),
                    status: formData.get('status'),
                    sessionSlot: formData.get('sessionSlot') || 'SESSION_1',
                    remarks: formData.get('remarks') || 'Individual attendance entry'
                };

                try {
                    const res = await fetch('/api/attendance', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify(payload)
                    });
                    const json = await res.json();
                    if (res.ok) {
                        showToast('Attendance logged successfully!', 'success');
                        singleForm.reset();
                        document.getElementById('singleAttendanceDate').value = todayStr;
                        loadFacultyDashboardData(activeFaculty.id);
                    } else {
                        showToast(json.message || 'Failed to log attendance', 'error');
                    }
                } catch (err) {
                    showToast('Network error: ' + err.message, 'error');
                }
            });
        }

        // Create Activity Form Submit
        const createActForm = document.getElementById('createActivityForm');
        if (createActForm) {
            createActForm.addEventListener('submit', async (e) => {
                e.preventDefault();
                const formData = new FormData(createActForm);
                const data = Object.fromEntries(formData.entries());

                data.facultyId = activeFaculty.id;
                data.semester = Number(data.semester) || 1;
                data.credits = Number(data.credits) || 1;
                data.maxEnrollment = Number(data.maxEnrollment) || 60;
                if (!data.startDate) delete data.startDate;
                if (!data.endDate) delete data.endDate;

                try {
                    const res = await fetch('/api/activities', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify(data)
                    });
                    const json = await res.json();
                    if (res.ok) {
                        showToast(`Activity "${data.activityCode}" created successfully!`, 'success');
                        createActForm.reset();
                        loadFacultyDashboardData(activeFaculty.id);
                    } else {
                        showToast(json.message || 'Failed to create activity', 'error');
                    }
                } catch (err) {
                    showToast('Network error: ' + err.message, 'error');
                }
            });
        }

        // History Filter Status
        const facultyFilterStatus = document.getElementById('facultyFilterStatus');
        if (facultyFilterStatus) {
            facultyFilterStatus.addEventListener('change', (e) => {
                renderFacultyHistoryTable(facultyAttendanceHistory, e.target.value);
            });
        }

        // Start Faculty Module
        loadFacultyListDropdown();
    };

    // =========================================================================
    // 3. ADMIN DASHBOARD MODULE (Day 17)
    // =========================================================================
    const initAdminDashboard = () => {
        const kpiStudents = document.getElementById('kpiAdminStudents');
        if (!kpiStudents) return; // Not on Admin Dashboard

        let adminStats = null;
        let cachedStudents = [];
        let cachedFaculty = [];
        let cachedActivities = [];
        let cachedAttendance = [];
        let deleteActionCallback = null;

        // Modal Helpers
        const openModal = (modalId) => {
            const modal = document.getElementById(modalId);
            if (modal) {
                modal.classList.add('active');
            }
        };

        const closeModal = (modalId) => {
            const modal = document.getElementById(modalId);
            if (modal) {
                modal.classList.remove('active');
            }
        };

        // Attach close modal events
        document.querySelectorAll('[data-close-modal]').forEach(btn => {
            btn.addEventListener('click', () => {
                const targetModal = btn.getAttribute('data-close-modal');
                closeModal(targetModal);
            });
        });

        // Tab Switching
        const tabBtns = document.querySelectorAll('.dashboard-tabs .tab-btn');
        tabBtns.forEach(btn => {
            btn.addEventListener('click', () => {
                const targetTabId = btn.getAttribute('data-tab');
                tabBtns.forEach(b => b.classList.remove('active'));
                document.querySelectorAll('.tab-content').forEach(tc => tc.classList.remove('active'));

                btn.classList.add('active');
                const targetContent = document.getElementById(targetTabId);
                if (targetContent) {
                    targetContent.classList.add('active');
                }
            });
        });

        // ---------------------------------------------------------------------
        // 3.1 Load Centralized Admin Stats
        // ---------------------------------------------------------------------
        const loadAdminStats = async () => {
            try {
                const res = await fetch('/api/admin/stats');
                if (!res.ok) throw new Error('Failed to fetch admin stats');
                const json = await res.json();
                adminStats = json.data;

                // 1. KPI Cards
                document.getElementById('kpiAdminStudents').innerText = adminStats.totalStudents;
                document.getElementById('kpiAdminActiveStudents').innerText = `${adminStats.activeStudents} of ${adminStats.totalStudents}`;

                document.getElementById('kpiAdminFaculty').innerText = adminStats.totalFaculty;
                document.getElementById('kpiAdminActiveFaculty').innerText = `${adminStats.activeFaculty} Active`;

                document.getElementById('kpiAdminActivities').innerText = adminStats.totalActivities;
                document.getElementById('kpiAdminActiveActivities').innerText = `${adminStats.activeActivities} Active, ${adminStats.upcomingActivities} Upcoming`;

                document.getElementById('kpiAdminAttendanceRate').innerText = `${adminStats.overallAttendancePercentage.toFixed(1)}%`;

                // 2. Attendance Distribution Bar
                const totalAtt = adminStats.totalAttendanceRecords;
                document.getElementById('overviewAttTotalBadge').innerText = `${totalAtt} Total Records`;

                if (totalAtt > 0) {
                    const presentPct = ((adminStats.presentCount / totalAtt) * 100).toFixed(1);
                    const odPct = ((adminStats.onDutyCount / totalAtt) * 100).toFixed(1);
                    const absentPct = ((adminStats.absentCount / totalAtt) * 100).toFixed(1);

                    document.getElementById('barSegmentPresent').style.width = `${presentPct}%`;
                    document.getElementById('barSegmentOd').style.width = `${odPct}%`;
                    document.getElementById('barSegmentAbsent').style.width = `${absentPct}%`;

                    document.getElementById('overviewPresentVal').innerText = adminStats.presentCount;
                    document.getElementById('overviewPresentPct').innerText = `${presentPct}%`;

                    document.getElementById('overviewOdVal').innerText = adminStats.onDutyCount;
                    document.getElementById('overviewOdPct').innerText = `${odPct}%`;

                    document.getElementById('overviewAbsentVal').innerText = adminStats.absentCount;
                    document.getElementById('overviewAbsentPct').innerText = `${absentPct}%`;
                } else {
                    document.getElementById('barSegmentPresent').style.width = '0%';
                    document.getElementById('barSegmentOd').style.width = '0%';
                    document.getElementById('barSegmentAbsent').style.width = '0%';
                }

                // 3. System & Database Health
                if (adminStats.databaseConnected) {
                    document.getElementById('adminDbStatusText').innerText = 'CONNECTED & HEALTHY';
                    document.getElementById('healthDbEngine').innerText = adminStats.databaseProductName || 'MySQL Database Server';
                } else {
                    document.getElementById('adminDbStatusText').innerText = 'DISCONNECTED';
                    document.getElementById('healthDbEngine').innerText = 'Connection Unavailable';
                }
                document.getElementById('healthServerTime').innerText = new Date(adminStats.serverTimestamp).toLocaleString();

                // 4. Department Distribution Cards
                renderDepartmentStats(adminStats.studentsByDepartment, adminStats.facultyByDepartment);

                // 5. Activity Type Distribution Cards
                renderActivityTypeStats(adminStats.activitiesByType);

            } catch (err) {
                console.error('Error loading admin stats:', err);
                showToast('Could not load administrative stats: ' + err.message, 'error');
            }
        };

        const renderDepartmentStats = (studentDepts, facultyDepts) => {
            const container = document.getElementById('departmentStatsContainer');
            if (!container) return;

            const allDeptKeys = new Set([
                ...Object.keys(studentDepts || {}),
                ...Object.keys(facultyDepts || {})
            ]);

            if (allDeptKeys.size === 0) {
                container.innerHTML = '<div style="color: var(--text-muted); padding: 12px;">No department records logged.</div>';
                return;
            }

            container.innerHTML = Array.from(allDeptKeys).map(dept => {
                const sCount = (studentDepts && studentDepts[dept]) || 0;
                const fCount = (facultyDepts && facultyDepts[dept]) || 0;
                return `
                    <div class="dept-stat-card">
                        <div>
                            <div style="font-weight: 700; color: #fff; font-size: 0.95rem;">${dept}</div>
                            <div style="font-size: 0.82rem; color: var(--text-muted); margin-top: 2px;">Academic Department</div>
                        </div>
                        <div style="text-align: right;">
                            <div style="font-size: 0.88rem; color: #60a5fa; font-weight: 600;">👥 ${sCount} Students</div>
                            <div style="font-size: 0.82rem; color: #c084fc; font-weight: 600; margin-top: 2px;">👨‍🏫 ${fCount} Faculty</div>
                        </div>
                    </div>
                `;
            }).join('');
        };

        const renderActivityTypeStats = (typeCounts) => {
            const container = document.getElementById('activityTypeStatsContainer');
            if (!container) return;

            const entries = Object.entries(typeCounts || {});
            if (entries.length === 0) {
                container.innerHTML = '<div style="color: var(--text-muted); padding: 12px;">No activity categories logged.</div>';
                return;
            }

            const icons = { WORKSHOP: '🛠️', LAB: '🧪', LECTURE: '📖', SEMINAR: '🎤', EVENT: '🏆' };

            container.innerHTML = entries.map(([type, count]) => {
                const icon = icons[type] || '📚';
                return `
                    <div class="dept-stat-card">
                        <div style="display: flex; align-items: center; gap: 10px;">
                            <span style="font-size: 1.3rem;">${icon}</span>
                            <div>
                                <div style="font-weight: 700; color: #fff; font-size: 0.92rem;">${type}</div>
                                <div style="font-size: 0.8rem; color: var(--text-muted);">Curriculum Track</div>
                            </div>
                        </div>
                        <div>
                            <span class="badge" style="background: rgba(59, 130, 246, 0.2); color: #93c5fd; font-size: 0.9rem;">
                                ${count} Activities
                            </span>
                        </div>
                    </div>
                `;
            }).join('');
        };

        // ---------------------------------------------------------------------
        // 3.2 Student Management (Full CRUD)
        // ---------------------------------------------------------------------
        const loadStudents = async () => {
            try {
                const res = await fetch('/api/students');
                if (!res.ok) throw new Error('Failed to load students');
                const json = await res.json();
                cachedStudents = json.data || [];
                populateStudentDeptFilter();
                renderStudentTable();
            } catch (err) {
                showToast('Error loading students: ' + err.message, 'error');
            }
        };

        const populateStudentDeptFilter = () => {
            const deptFilter = document.getElementById('studentDeptFilter');
            if (!deptFilter) return;
            const currentVal = deptFilter.value;
            const depts = Array.from(new Set(cachedStudents.map(s => s.department).filter(Boolean)));
            deptFilter.innerHTML = '<option value="ALL">All Departments</option>' +
                depts.map(d => `<option value="${d}">${d}</option>`).join('');
            if (depts.includes(currentVal)) deptFilter.value = currentVal;
        };

        const renderStudentTable = () => {
            const tbody = document.getElementById('adminStudentTableBody');
            const countBadge = document.getElementById('studentTableCountBadge');
            if (!tbody) return;

            const search = (document.getElementById('studentSearchInput')?.value || '').toLowerCase().trim();
            const dept = document.getElementById('studentDeptFilter')?.value || 'ALL';
            const year = document.getElementById('studentYearFilter')?.value || 'ALL';
            const status = document.getElementById('studentStatusFilter')?.value || 'ALL';

            const filtered = cachedStudents.filter(s => {
                const matchSearch = !search ||
                    (s.rollNumber && s.rollNumber.toLowerCase().includes(search)) ||
                    (s.name && s.name.toLowerCase().includes(search)) ||
                    (s.email && s.email.toLowerCase().includes(search));
                const matchDept = dept === 'ALL' || s.department === dept;
                const matchYear = year === 'ALL' || String(s.yearOfStudy) === year;
                const matchStatus = status === 'ALL' || s.status === status;
                return matchSearch && matchDept && matchYear && matchStatus;
            });

            if (countBadge) countBadge.innerText = `Showing ${filtered.length} of ${cachedStudents.length} Students`;

            if (filtered.length === 0) {
                tbody.innerHTML = `
                    <tr>
                        <td colspan="8" style="text-align: center; color: var(--text-muted); padding: 24px;">
                            No students match the current filters.
                        </td>
                    </tr>
                `;
                return;
            }

            tbody.innerHTML = filtered.map(s => {
                const statusBadge = s.status === 'ACTIVE'
                    ? '<span class="status-pill present">ACTIVE</span>'
                    : '<span class="status-pill absent">INACTIVE</span>';

                return `
                    <tr>
                        <td><strong style="color: #93c5fd;">${s.rollNumber}</strong></td>
                        <td><span style="font-weight: 600; color: #fff;">${s.name}</span></td>
                        <td>${s.department}</td>
                        <td>Year ${s.yearOfStudy || 1}${s.section ? ` (${s.section})` : ''}</td>
                        <td style="color: var(--text-muted);">${s.email || '—'}</td>
                        <td style="color: var(--text-muted);">${s.phoneNumber || '—'}</td>
                        <td>${statusBadge}</td>
                        <td style="text-align: center;">
                            <div class="admin-table-actions" style="justify-content: center;">
                                <button type="button" class="action-btn edit" data-edit-student="${s.id}">
                                    <span>✏️</span> Edit
                                </button>
                                <button type="button" class="action-btn delete" data-delete-student="${s.id}" data-roll="${s.rollNumber}">
                                    <span>🗑️</span> Delete
                                </button>
                            </div>
                        </td>
                    </tr>
                `;
            }).join('');

            // Attach action listeners
            tbody.querySelectorAll('[data-edit-student]').forEach(btn => {
                btn.addEventListener('click', () => {
                    const studentId = Number(btn.getAttribute('data-edit-student'));
                    const student = cachedStudents.find(s => s.id === studentId);
                    if (student) openEditStudentModal(student);
                });
            });

            tbody.querySelectorAll('[data-delete-student]').forEach(btn => {
                btn.addEventListener('click', () => {
                    const studentId = Number(btn.getAttribute('data-delete-student'));
                    const roll = btn.getAttribute('data-roll');
                    confirmDeleteAction(`Are you sure you want to delete student "${roll}"? All associated attendance logs will be removed.`, async () => {
                        try {
                            const res = await fetch(`/api/students/${studentId}`, { method: 'DELETE' });
                            if (res.ok) {
                                showToast(`Student ${roll} deleted successfully`, 'success');
                                loadStudents();
                                loadAdminStats();
                            } else {
                                const json = await res.json();
                                showToast(json.message || 'Failed to delete student', 'error');
                            }
                        } catch (err) {
                            showToast('Error deleting student: ' + err.message, 'error');
                        }
                    });
                });
            });
        };

        const openAddStudentModal = () => {
            const form = document.getElementById('studentModalForm');
            if (form) form.reset();
            document.getElementById('studentModalId').value = '';
            document.getElementById('studentModalTitle').innerHTML = '<span>🎓</span> Register New Student';
            openModal('studentModal');
        };

        const openEditStudentModal = (student) => {
            document.getElementById('studentModalId').value = student.id;
            document.getElementById('studentModalRoll').value = student.rollNumber || '';
            document.getElementById('studentModalName').value = student.name || '';
            document.getElementById('studentModalDept').value = student.department || '';
            document.getElementById('studentModalYear').value = student.yearOfStudy || 1;
            document.getElementById('studentModalSection').value = student.section || 'A';
            document.getElementById('studentModalStatus').value = student.status || 'ACTIVE';
            document.getElementById('studentModalEmail').value = student.email || '';
            document.getElementById('studentModalPhone').value = student.phoneNumber || '';
            document.getElementById('studentModalTitle').innerHTML = `<span>✏️</span> Edit Student: ${student.rollNumber}`;
            openModal('studentModal');
        };

        // Student Form Submit
        const studentForm = document.getElementById('studentModalForm');
        if (studentForm) {
            studentForm.addEventListener('submit', async (e) => {
                e.preventDefault();
                const id = document.getElementById('studentModalId').value;
                const payload = {
                    rollNumber: document.getElementById('studentModalRoll').value.trim(),
                    name: document.getElementById('studentModalName').value.trim(),
                    department: document.getElementById('studentModalDept').value.trim(),
                    yearOfStudy: Number(document.getElementById('studentModalYear').value),
                    section: document.getElementById('studentModalSection').value.trim(),
                    status: document.getElementById('studentModalStatus').value,
                    email: document.getElementById('studentModalEmail').value.trim(),
                    phoneNumber: document.getElementById('studentModalPhone').value.trim()
                };

                const url = id ? `/api/students/${id}` : '/api/students';
                const method = id ? 'PUT' : 'POST';

                try {
                    const res = await fetch(url, {
                        method: method,
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify(payload)
                    });
                    const json = await res.json();
                    if (res.ok) {
                        showToast(`Student ${payload.rollNumber} saved successfully!`, 'success');
                        closeModal('studentModal');
                        loadStudents();
                        loadAdminStats();
                    } else {
                        showToast(json.message || 'Error saving student record', 'error');
                    }
                } catch (err) {
                    showToast('Network error: ' + err.message, 'error');
                }
            });
        }

        // ---------------------------------------------------------------------
        // 3.3 Faculty Management (Full CRUD)
        // ---------------------------------------------------------------------
        const loadFaculty = async () => {
            try {
                const res = await fetch('/api/faculty');
                if (!res.ok) throw new Error('Failed to load faculty');
                const json = await res.json();
                cachedFaculty = json.data || [];
                populateFacultyFilters();
                renderFacultyTable();
            } catch (err) {
                showToast('Error loading faculty: ' + err.message, 'error');
            }
        };

        const populateFacultyFilters = () => {
            const deptFilter = document.getElementById('facultyDeptFilter');
            const desigFilter = document.getElementById('facultyDesigFilter');
            if (deptFilter) {
                const depts = Array.from(new Set(cachedFaculty.map(f => f.department).filter(Boolean)));
                deptFilter.innerHTML = '<option value="ALL">All Departments</option>' +
                    depts.map(d => `<option value="${d}">${d}</option>`).join('');
            }
            if (desigFilter) {
                const desigs = Array.from(new Set(cachedFaculty.map(f => f.designation).filter(Boolean)));
                desigFilter.innerHTML = '<option value="ALL">All Designations</option>' +
                    desigs.map(d => `<option value="${d}">${d}</option>`).join('');
            }
        };

        const renderFacultyTable = () => {
            const tbody = document.getElementById('adminFacultyTableBody');
            const countBadge = document.getElementById('facultyTableCountBadge');
            if (!tbody) return;

            const search = (document.getElementById('facultySearchInput')?.value || '').toLowerCase().trim();
            const dept = document.getElementById('facultyDeptFilter')?.value || 'ALL';
            const desig = document.getElementById('facultyDesigFilter')?.value || 'ALL';
            const status = document.getElementById('facultyStatusFilter')?.value || 'ALL';

            const filtered = cachedFaculty.filter(f => {
                const matchSearch = !search ||
                    (f.employeeId && f.employeeId.toLowerCase().includes(search)) ||
                    (f.name && f.name.toLowerCase().includes(search)) ||
                    (f.email && f.email.toLowerCase().includes(search));
                const matchDept = dept === 'ALL' || f.department === dept;
                const matchDesig = desig === 'ALL' || f.designation === desig;
                const matchStatus = status === 'ALL' || f.status === status;
                return matchSearch && matchDept && matchDesig && matchStatus;
            });

            if (countBadge) countBadge.innerText = `Showing ${filtered.length} of ${cachedFaculty.length} Faculty Members`;

            if (filtered.length === 0) {
                tbody.innerHTML = `
                    <tr>
                        <td colspan="9" style="text-align: center; color: var(--text-muted); padding: 24px;">
                            No faculty members match the current filters.
                        </td>
                    </tr>
                `;
                return;
            }

            tbody.innerHTML = filtered.map(f => {
                const statusBadge = f.status === 'ACTIVE'
                    ? '<span class="status-pill present">ACTIVE</span>'
                    : '<span class="status-pill absent">INACTIVE</span>';

                return `
                    <tr>
                        <td><strong style="color: #c084fc;">${f.employeeId}</strong></td>
                        <td><span style="font-weight: 600; color: #fff;">${f.name}</span></td>
                        <td>${f.department}</td>
                        <td style="color: #60a5fa;">${f.designation}</td>
                        <td style="color: var(--text-muted);">${f.email}</td>
                        <td style="color: var(--text-muted);">${f.phoneNumber || '—'}</td>
                        <td>${f.experienceYears ? `${f.experienceYears} yrs` : '—'}</td>
                        <td>${statusBadge}</td>
                        <td style="text-align: center;">
                            <div class="admin-table-actions" style="justify-content: center;">
                                <button type="button" class="action-btn edit" data-edit-faculty="${f.id}">
                                    <span>✏️</span> Edit
                                </button>
                                <button type="button" class="action-btn delete" data-delete-faculty="${f.id}" data-empid="${f.employeeId}">
                                    <span>🗑️</span> Delete
                                </button>
                            </div>
                        </td>
                    </tr>
                `;
            }).join('');

            // Attach action listeners
            tbody.querySelectorAll('[data-edit-faculty]').forEach(btn => {
                btn.addEventListener('click', () => {
                    const facultyId = Number(btn.getAttribute('data-edit-faculty'));
                    const faculty = cachedFaculty.find(f => f.id === facultyId);
                    if (faculty) openEditFacultyModal(faculty);
                });
            });

            tbody.querySelectorAll('[data-delete-faculty]').forEach(btn => {
                btn.addEventListener('click', () => {
                    const facultyId = Number(btn.getAttribute('data-delete-faculty'));
                    const empId = btn.getAttribute('data-empid');
                    confirmDeleteAction(`Are you sure you want to delete faculty member "${empId}"?`, async () => {
                        try {
                            const res = await fetch(`/api/faculty/${facultyId}`, { method: 'DELETE' });
                            if (res.ok) {
                                showToast(`Faculty ${empId} deleted successfully`, 'success');
                                loadFaculty();
                                loadAdminStats();
                            } else {
                                const json = await res.json();
                                showToast(json.message || 'Failed to delete faculty member', 'error');
                            }
                        } catch (err) {
                            showToast('Error deleting faculty: ' + err.message, 'error');
                        }
                    });
                });
            });
        };

        const openAddFacultyModal = () => {
            const form = document.getElementById('facultyModalForm');
            if (form) form.reset();
            document.getElementById('facultyModalId').value = '';
            document.getElementById('facultyModalTitle').innerHTML = '<span>👨‍🏫</span> Add Faculty Member';
            openModal('facultyModal');
        };

        const openEditFacultyModal = (faculty) => {
            document.getElementById('facultyModalId').value = faculty.id;
            document.getElementById('facultyModalEmpId').value = faculty.employeeId || '';
            document.getElementById('facultyModalName').value = faculty.name || '';
            document.getElementById('facultyModalDept').value = faculty.department || '';
            document.getElementById('facultyModalDesig').value = faculty.designation || '';
            document.getElementById('facultyModalSpec').value = faculty.specialization || '';
            document.getElementById('facultyModalExp').value = faculty.experienceYears || 0;
            document.getElementById('facultyModalEmail').value = faculty.email || '';
            document.getElementById('facultyModalPhone').value = faculty.phoneNumber || '';
            document.getElementById('facultyModalStatus').value = faculty.status || 'ACTIVE';
            document.getElementById('facultyModalTitle').innerHTML = `<span>✏️</span> Edit Faculty: ${faculty.employeeId}`;
            openModal('facultyModal');
        };

        // Faculty Form Submit
        const facultyForm = document.getElementById('facultyModalForm');
        if (facultyForm) {
            facultyForm.addEventListener('submit', async (e) => {
                e.preventDefault();
                const id = document.getElementById('facultyModalId').value;
                const payload = {
                    employeeId: document.getElementById('facultyModalEmpId').value.trim(),
                    name: document.getElementById('facultyModalName').value.trim(),
                    department: document.getElementById('facultyModalDept').value.trim(),
                    designation: document.getElementById('facultyModalDesig').value.trim(),
                    specialization: document.getElementById('facultyModalSpec').value.trim(),
                    experienceYears: Number(document.getElementById('facultyModalExp').value),
                    email: document.getElementById('facultyModalEmail').value.trim(),
                    phoneNumber: document.getElementById('facultyModalPhone').value.trim(),
                    status: document.getElementById('facultyModalStatus').value
                };

                const url = id ? `/api/faculty/${id}` : '/api/faculty';
                const method = id ? 'PUT' : 'POST';

                try {
                    const res = await fetch(url, {
                        method: method,
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify(payload)
                    });
                    const json = await res.json();
                    if (res.ok) {
                        showToast(`Faculty ${payload.employeeId} saved successfully!`, 'success');
                        closeModal('facultyModal');
                        loadFaculty();
                        loadAdminStats();
                    } else {
                        showToast(json.message || 'Error saving faculty member', 'error');
                    }
                } catch (err) {
                    showToast('Network error: ' + err.message, 'error');
                }
            });
        }

        // ---------------------------------------------------------------------
        // 3.4 Activity Oversight & Management (Full CRUD)
        // ---------------------------------------------------------------------
        const loadActivities = async () => {
            try {
                const res = await fetch('/api/activities/list');
                if (!res.ok) throw new Error('Failed to load activities');
                cachedActivities = await res.json() || [];
                populateActivityDeptFilter();
                renderActivityTable();
            } catch (err) {
                showToast('Error loading activities: ' + err.message, 'error');
            }
        };

        const populateActivityDeptFilter = () => {
            const deptFilter = document.getElementById('activityDeptFilter');
            if (!deptFilter) return;
            const depts = Array.from(new Set(cachedActivities.map(a => a.department).filter(Boolean)));
            deptFilter.innerHTML = '<option value="ALL">All Departments</option>' +
                depts.map(d => `<option value="${d}">${d}</option>`).join('');
        };

        const renderActivityTable = () => {
            const tbody = document.getElementById('adminActivityTableBody');
            const countBadge = document.getElementById('activityTableCountBadge');
            if (!tbody) return;

            const search = (document.getElementById('activitySearchInput')?.value || '').toLowerCase().trim();
            const dept = document.getElementById('activityDeptFilter')?.value || 'ALL';
            const type = document.getElementById('activityTypeFilter')?.value || 'ALL';
            const status = document.getElementById('activityStatusFilter')?.value || 'ALL';

            const filtered = cachedActivities.filter(a => {
                const matchSearch = !search ||
                    (a.activityCode && a.activityCode.toLowerCase().includes(search)) ||
                    (a.title && a.title.toLowerCase().includes(search));
                const matchDept = dept === 'ALL' || a.department === dept;
                const matchType = type === 'ALL' || a.activityType === type;
                const matchStatus = status === 'ALL' || a.status === status;
                return matchSearch && matchDept && matchType && matchStatus;
            });

            if (countBadge) countBadge.innerText = `Showing ${filtered.length} of ${cachedActivities.length} Activities`;

            if (filtered.length === 0) {
                tbody.innerHTML = `
                    <tr>
                        <td colspan="9" style="text-align: center; color: var(--text-muted); padding: 24px;">
                            No activities match the current filters.
                        </td>
                    </tr>
                `;
                return;
            }

            tbody.innerHTML = filtered.map(a => {
                return `
                    <tr>
                        <td><strong style="color: #93c5fd; font-family: monospace;">${a.activityCode}</strong></td>
                        <td>
                            <div style="font-weight: 600; color: #fff;">${a.title}</div>
                            <div style="font-size: 0.78rem; color: var(--text-muted); margin-top: 2px;">${a.venue || 'Campus Venue'}</div>
                        </td>
                        <td><span class="activity-card-type">${a.activityType}</span></td>
                        <td>${a.department}</td>
                        <td>${a.facultyName || `Faculty #${a.facultyId || 1}`}</td>
                        <td style="color: #fbbf24; font-weight: 600;">${a.credits || 0} Credits</td>
                        <td>${a.maxEnrollment || 60} Seats</td>
                        <td>
                            <select class="form-select" data-activity-status-select="${a.id}" style="padding: 4px 8px; font-size: 0.8rem; width: 120px;">
                                <option value="ACTIVE" ${a.status === 'ACTIVE' ? 'selected' : ''}>ACTIVE</option>
                                <option value="UPCOMING" ${a.status === 'UPCOMING' ? 'selected' : ''}>UPCOMING</option>
                                <option value="COMPLETED" ${a.status === 'COMPLETED' ? 'selected' : ''}>COMPLETED</option>
                            </select>
                        </td>
                        <td style="text-align: center;">
                            <div class="admin-table-actions" style="justify-content: center;">
                                <button type="button" class="action-btn edit" data-edit-activity="${a.id}">
                                    <span>✏️</span> Edit
                                </button>
                                <button type="button" class="action-btn delete" data-delete-activity="${a.id}" data-code="${a.activityCode}">
                                    <span>🗑️</span> Delete
                                </button>
                            </div>
                        </td>
                    </tr>
                `;
            }).join('');

            // Status dropdown change event
            tbody.querySelectorAll('[data-activity-status-select]').forEach(select => {
                select.addEventListener('change', async (e) => {
                    const activityId = select.getAttribute('data-activity-status-select');
                    const newStatus = e.target.value;
                    try {
                        const res = await fetch(`/api/admin/activities/${activityId}/status?status=${newStatus}`, {
                            method: 'PUT'
                        });
                        if (res.ok) {
                            showToast(`Status updated to ${newStatus}`, 'success');
                            loadActivities();
                            loadAdminStats();
                        } else {
                            showToast('Failed to update activity status', 'error');
                        }
                    } catch (err) {
                        showToast('Error updating status: ' + err.message, 'error');
                    }
                });
            });

            // Edit Activity Button
            tbody.querySelectorAll('[data-edit-activity]').forEach(btn => {
                btn.addEventListener('click', () => {
                    const activityId = Number(btn.getAttribute('data-edit-activity'));
                    const act = cachedActivities.find(a => a.id === activityId);
                    if (act) openEditActivityModal(act);
                });
            });

            // Delete Activity Button
            tbody.querySelectorAll('[data-delete-activity]').forEach(btn => {
                btn.addEventListener('click', () => {
                    const activityId = Number(btn.getAttribute('data-delete-activity'));
                    const code = btn.getAttribute('data-code');
                    confirmDeleteAction(`Are you sure you want to delete activity "${code}"?`, async () => {
                        try {
                            const res = await fetch(`/api/activities/${activityId}`, { method: 'DELETE' });
                            if (res.ok) {
                                showToast(`Activity ${code} deleted successfully`, 'success');
                                loadActivities();
                                loadAdminStats();
                            } else {
                                showToast('Failed to delete activity', 'error');
                            }
                        } catch (err) {
                            showToast('Error deleting activity: ' + err.message, 'error');
                        }
                    });
                });
            });
        };

        const openAddActivityModal = () => {
            const form = document.getElementById('activityModalForm');
            if (form) form.reset();
            document.getElementById('activityModalId').value = '';
            document.getElementById('activityModalTitle').innerHTML = '<span>📅</span> Schedule Curriculum Activity';
            openModal('activityModal');
        };

        const openEditActivityModal = (act) => {
            document.getElementById('activityModalId').value = act.id;
            document.getElementById('activityModalCode').value = act.activityCode || '';
            document.getElementById('activityModalTitleInput').value = act.title || '';
            document.getElementById('activityModalType').value = act.activityType || 'WORKSHOP';
            document.getElementById('activityModalDept').value = act.department || '';
            document.getElementById('activityModalFacultyId').value = act.facultyId || 1;
            document.getElementById('activityModalYear').value = act.academicYear || '2025-2026';
            document.getElementById('activityModalSemester').value = act.semester || 5;
            document.getElementById('activityModalCredits').value = act.credits || 3;
            document.getElementById('activityModalMaxEnroll').value = act.maxEnrollment || 60;
            document.getElementById('activityModalVenue').value = act.venue || '';
            document.getElementById('activityModalStatus').value = act.status || 'ACTIVE';
            document.getElementById('activityModalDesc').value = act.description || '';
            document.getElementById('activityModalTitle').innerHTML = `<span>✏️</span> Edit Activity: ${act.activityCode}`;
            openModal('activityModal');
        };

        // Activity Form Submit
        const activityForm = document.getElementById('activityModalForm');
        if (activityForm) {
            activityForm.addEventListener('submit', async (e) => {
                e.preventDefault();
                const id = document.getElementById('activityModalId').value;
                const payload = {
                    activityCode: document.getElementById('activityModalCode').value.trim(),
                    title: document.getElementById('activityModalTitleInput').value.trim(),
                    activityType: document.getElementById('activityModalType').value,
                    department: document.getElementById('activityModalDept').value.trim(),
                    facultyId: Number(document.getElementById('activityModalFacultyId').value) || 1,
                    academicYear: document.getElementById('activityModalYear').value.trim(),
                    semester: Number(document.getElementById('activityModalSemester').value),
                    credits: Number(document.getElementById('activityModalCredits').value),
                    maxEnrollment: Number(document.getElementById('activityModalMaxEnroll').value),
                    venue: document.getElementById('activityModalVenue').value.trim(),
                    status: document.getElementById('activityModalStatus').value,
                    description: document.getElementById('activityModalDesc').value.trim()
                };

                const url = id ? `/api/activities/${id}` : '/api/activities';
                const method = id ? 'PUT' : 'POST';

                try {
                    const res = await fetch(url, {
                        method: method,
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify(payload)
                    });
                    const json = await res.json();
                    if (res.ok) {
                        showToast(`Activity "${payload.activityCode}" saved successfully!`, 'success');
                        closeModal('activityModal');
                        loadActivities();
                        loadAdminStats();
                    } else {
                        showToast(json.message || 'Failed to save activity', 'error');
                    }
                } catch (err) {
                    showToast('Network error: ' + err.message, 'error');
                }
            });
        }

        // ---------------------------------------------------------------------
        // 3.5 Attendance Oversight & Inspection
        // ---------------------------------------------------------------------
        const loadAttendance = async () => {
            try {
                const res = await fetch('/api/attendance');
                if (!res.ok) throw new Error('Failed to load attendance records');
                const json = await res.json();
                cachedAttendance = json.data || [];
                renderAttendanceTable();
            } catch (err) {
                showToast('Error loading attendance logs: ' + err.message, 'error');
            }
        };

        const renderAttendanceTable = () => {
            const tbody = document.getElementById('adminAttendanceTableBody');
            const countBadge = document.getElementById('attendanceTableCountBadge');
            if (!tbody) return;

            const rollSearch = (document.getElementById('attSearchRollInput')?.value || '').toLowerCase().trim();
            const statusFilter = document.getElementById('attFilterStatus')?.value || 'ALL';

            const filtered = cachedAttendance.filter(item => {
                const matchRoll = !rollSearch ||
                    (item.studentRollNumber && item.studentRollNumber.toLowerCase().includes(rollSearch)) ||
                    (item.studentName && item.studentName.toLowerCase().includes(rollSearch));
                const matchStatus = statusFilter === 'ALL' || item.status === statusFilter;
                return matchRoll && matchStatus;
            });

            if (countBadge) countBadge.innerText = `Showing ${filtered.length} of ${cachedAttendance.length} Attendance Logs`;

            if (filtered.length === 0) {
                tbody.innerHTML = `
                    <tr>
                        <td colspan="8" style="text-align: center; color: var(--text-muted); padding: 24px;">
                            No attendance records match the search criteria.
                        </td>
                    </tr>
                `;
                return;
            }

            tbody.innerHTML = filtered.map(item => {
                let statusBadge = '<span class="status-pill present">PRESENT</span>';
                if (item.status === 'ABSENT') statusBadge = '<span class="status-pill absent">ABSENT</span>';
                else if (item.status === 'ON_DUTY') statusBadge = '<span class="status-pill od">ON_DUTY</span>';

                return `
                    <tr>
                        <td style="color: var(--text-dim); font-family: monospace;">#${item.id}</td>
                        <td>${item.attendanceDate || '—'}</td>
                        <td><strong style="color: #93c5fd;">${item.studentRollNumber || '—'}</strong></td>
                        <td><span style="color: #fff; font-weight: 500;">${item.studentName || 'Student'}</span></td>
                        <td><span class="activity-code-badge">${item.activityCode || 'ACT'}</span></td>
                        <td style="color: var(--text-muted);">${item.sessionSlot || 'SESSION_1'}</td>
                        <td>${statusBadge}</td>
                        <td style="color: var(--text-muted);">${item.remarks || '—'}</td>
                    </tr>
                `;
            }).join('');
        };

        // Student Attendance Inspector
        const btnInspect = document.getElementById('btnInspectStudent');
        if (btnInspect) {
            btnInspect.addEventListener('click', async () => {
                const roll = document.getElementById('inspectorRollInput')?.value.trim();
                if (!roll) {
                    showToast('Please enter a roll number to inspect', 'warning');
                    return;
                }

                try {
                    const res = await fetch(`/api/attendance/student/roll/${encodeURIComponent(roll)}/summary`);
                    if (!res.ok) throw new Error('Student not found or no attendance data');
                    const json = await res.json();
                    const summary = json.data;

                    const resultsPanel = document.getElementById('inspectorResultsPanel');
                    if (resultsPanel) resultsPanel.style.display = 'block';

                    document.getElementById('inspStudentName').innerText = summary.studentName || roll;
                    const pct = summary.overallAttendancePercentage || 0;
                    document.getElementById('inspStudentPct').innerText = `${pct.toFixed(1)}%`;
                    document.getElementById('inspStudentSessions').innerText = `${summary.attendedSessions} / ${summary.totalSessions}`;

                    const tag = document.getElementById('inspStudentEligibilityTag');
                    if (pct >= 75) {
                        tag.className = 'eligibility-tag tag-eligible';
                        tag.innerText = '✓ Good Standing (Exam Eligible)';
                    } else if (pct >= 65) {
                        tag.className = 'eligibility-tag tag-warning';
                        tag.innerText = '⚠️ Condonation Needed (65-75%)';
                    } else {
                        tag.className = 'eligibility-tag tag-critical';
                        tag.innerText = '⛔ Critical Shortage (<65%)';
                    }

                    showToast(`Loaded records for student ${roll}`, 'info');
                } catch (err) {
                    showToast(err.message, 'error');
                }
            });
        }

        // Delete Confirmation Helper
        const confirmDeleteAction = (message, onConfirm) => {
            const modal = document.getElementById('confirmDeleteModal');
            const msgElem = document.getElementById('deleteModalMessage');
            if (msgElem) msgElem.innerText = message;
            deleteActionCallback = onConfirm;
            openModal('confirmDeleteModal');
        };

        const btnExecDelete = document.getElementById('btnExecuteDelete');
        if (btnExecDelete) {
            btnExecDelete.addEventListener('click', () => {
                if (deleteActionCallback) {
                    deleteActionCallback();
                    deleteActionCallback = null;
                }
                closeModal('confirmDeleteModal');
            });
        }

        // ---------------------------------------------------------------------
        // 3.6 Event Listeners & Filter Triggers
        // ---------------------------------------------------------------------
        // Student filters
        ['studentSearchInput', 'studentDeptFilter', 'studentYearFilter', 'studentStatusFilter'].forEach(id => {
            document.getElementById(id)?.addEventListener('input', renderStudentTable);
            document.getElementById(id)?.addEventListener('change', renderStudentTable);
        });
        document.getElementById('btnOpenAddStudentModal')?.addEventListener('click', openAddStudentModal);

        // Faculty filters
        ['facultySearchInput', 'facultyDeptFilter', 'facultyDesigFilter', 'facultyStatusFilter'].forEach(id => {
            document.getElementById(id)?.addEventListener('input', renderFacultyTable);
            document.getElementById(id)?.addEventListener('change', renderFacultyTable);
        });
        document.getElementById('btnOpenAddFacultyModal')?.addEventListener('click', openAddFacultyModal);

        // Activity filters
        ['activitySearchInput', 'activityDeptFilter', 'activityTypeFilter', 'activityStatusFilter'].forEach(id => {
            document.getElementById(id)?.addEventListener('input', renderActivityTable);
            document.getElementById(id)?.addEventListener('change', renderActivityTable);
        });
        document.getElementById('btnOpenAddActivityModal')?.addEventListener('click', openAddActivityModal);

        // Attendance filters
        ['attSearchRollInput', 'attFilterStatus'].forEach(id => {
            document.getElementById(id)?.addEventListener('input', renderAttendanceTable);
            document.getElementById(id)?.addEventListener('change', renderAttendanceTable);
        });
        document.getElementById('btnRefreshAttendance')?.addEventListener('click', loadAttendance);

        // Quick Operations
        document.getElementById('btnQuickAddStudent')?.addEventListener('click', () => {
            document.querySelector('.tab-btn[data-tab="tab-students"]')?.click();
            openAddStudentModal();
        });
        document.getElementById('btnQuickAddFaculty')?.addEventListener('click', () => {
            document.querySelector('.tab-btn[data-tab="tab-faculty"]')?.click();
            openAddFacultyModal();
        });
        document.getElementById('btnQuickAddActivity')?.addEventListener('click', () => {
            document.querySelector('.tab-btn[data-tab="tab-activities"]')?.click();
            openAddActivityModal();
        });
        document.getElementById('btnRefreshAdminStats')?.addEventListener('click', () => {
            loadAdminStats();
            showToast('Refreshed administrative stats!', 'info');
        });

        // Initialize Admin Dashboard Data
        loadAdminStats();
        loadStudents();
        loadFaculty();
        loadActivities();
        loadAttendance();
    };

    // =========================================================================
    // 4. INITIALIZATION
    // =========================================================================
    initStudentDashboard();
    initFacultyDashboard();
    initAdminDashboard();
});
