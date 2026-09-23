/**
 * Smart Curriculum Activity & Attendance Portal - client side script
 * Handles Student CRUD form submissions and communicates with the Spring Boot REST API.
 */

document.addEventListener('DOMContentLoaded', () => {
    const showMessage = (msg, isError = false) => {
        // Simple alert; replace with toast UI if desired
        alert(msg);
    };

    // CREATE student
    const createForm = document.getElementById('createStudentForm');
    if (createForm) {
        createForm.addEventListener('submit', async e => {
            e.preventDefault();
            const data = Object.fromEntries(new FormData(createForm).entries());
            try {
                const response = await fetch('/api/students', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(data)
                });
                const result = await response.json();
                if (response.ok) {
                    showMessage('Student created successfully.');
                    createForm.reset();
                } else {
                    showMessage('Error: ' + (result.message || 'Failed to create student'), true);
                }
            } catch (err) {
                showMessage('Network error: ' + err.message, true);
            }
        });
    }

    // UPDATE student
    const updateForm = document.getElementById('updateStudentForm');
    if (updateForm) {
        updateForm.addEventListener('submit', async e => {
            e.preventDefault();
            const formData = new FormData(updateForm);
            const id = formData.get('id');
            const data = {};
            for (const [key, value] of formData.entries()) {
                if (key !== 'id' && value) {
                    data[key] = value;
                }
            }
            if (!id) {
                showMessage('Student ID is required for update.', true);
                return;
            }
            try {
                const response = await fetch(`/api/students/${id}`, {
                    method: 'PUT',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(data)
                });
                const result = await response.json();
                if (response.ok) {
                    showMessage('Student updated successfully.');
                    updateForm.reset();
                } else {
                    showMessage('Error: ' + (result.message || 'Failed to update student'), true);
                }
            } catch (err) {
                showMessage('Network error: ' + err.message, true);
            }
        });
    }

    // DELETE student
    const deleteForm = document.getElementById('deleteStudentForm');
    if (deleteForm) {
        deleteForm.addEventListener('submit', async e => {
            e.preventDefault();
            const id = deleteForm.querySelector('input[name="id"]').value;
            if (!id) {
                showMessage('Student ID is required for deletion.', true);
                return;
            }
            if (!confirm('Are you sure you want to delete student ID ' + id + '?')) {
                return;
            }
            try {
                const response = await fetch(`/api/students/${id}`, {
                    method: 'DELETE'
                });
                const result = await response.json();
                if (response.ok) {
                    showMessage('Student deleted successfully.');
                    deleteForm.reset();
                } else {
                    showMessage('Error: ' + (result.message || 'Failed to delete student'), true);
                }
            } catch (err) {
                showMessage('Network error: ' + err.message, true);
            }
        });
    }
});
