let formEdit = document.forms["form_Edit"];

async function editModal(id) {
    const modalEdit = new bootstrap.Modal(document.querySelector('#editModal'));
    await open_fill_modal(formEdit, modalEdit, id);
    editUser();
}

function editUser() {
    formEdit.addEventListener("submit", ev => {
        ev.preventDefault();

        fetch(URLEdit + formEdit.id.value, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                id: formEdit.id.value,
                firstName: formEdit.firstName.value,
                lastName: formEdit.lastName.value,
                email: formEdit.email.value,
                password: formEdit.password.value,
                roles: []
            })
        }).then(() => {
            $('#edit_close').click();
            fetch(URLEdit)
                .then(response => response.json())
                .then(arrayUsers => fillTable(arrayUsers))
                .catch(error => console.log(error));
        });
    });
}

function loadRolesForEdit() {
    let formEdit = document.getElementById("div_edit");
    fetch("http://localhost:8080/api/roles")
        .then(res => res.json())
        .then(list_Roles => {
            list_Roles.forEach(role => {
                let labell = document.createElement("label");
                labell.for = 'role_Edit_' + role.id;
                labell.innerText = role.name + ' ';
                formEdit.appendChild(labell);
                let inputt = document.createElement("input");
                inputt.id = 'role_Edit_' + role.id;
                inputt.value = role.id;
                inputt.type = 'checkbox';
                inputt.name = 'selectedRoles';
                inputt.text = role.name;
                formEdit.appendChild(inputt);
                let xxx = document.createElement("br");
                formEdit.appendChild(xxx);
            });
        })
        .catch(error => console.error(error));
}

//добавление checbox с ролями в модальное окно Edit при загрузке страницы
window.addEventListener("load", loadRolesForEdit);