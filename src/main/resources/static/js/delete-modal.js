let formDelete = document.forms["form_Delete"];

async function deleteModal(id) {
    const modalDelete = new bootstrap.Modal(document.querySelector('#deleteModal'));
    await open_fill_modal(formDelete, modalDelete, id);
    let userModal = await getUserById(id);
    let nameInputs = formDelete.querySelectorAll("[name='selectedRoles']");
    for (let i = 0; i < nameInputs.length; i++) {
        for(let j = 0; j < userModal.roles.length;j++)
            if (nameInputs[i].id === 'role_Delete_' + userModal.roles[j].id) {
                nameInputs[i].checked = true;
                //rolesForEdit.push(user.roles[j]);
            }
    }
    deleteUser();
}

function deleteUser() {
    formDelete.addEventListener("submit", ev => {
        ev.preventDefault();
        fetch("http://localhost:8080/api/admin/" + formDelete.id.value, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(() => {
            $('#delete_close').click();
            fetch(URLEdit)
                .then(response => response.json())
                .then(arrayUsers => fillTable(arrayUsers))
                .catch(error => console.log(error));
        });
    });
}

function loadRolesForDelete() {
    let formDelete = document.getElementById("div_delete");
    fetch("http://localhost:8080/api/roles")
        .then(res => res.json())
        .then(list_Roles => {
            list_Roles.forEach(role => {
                let labell = document.createElement("label");
                labell.for = 'role_Delete_' + role.id;
                labell.innerText = role.name + ' ';
                formDelete.appendChild(labell);
                let inputt = document.createElement("input");
                inputt.id = 'role_Delete_' + role.id;
                inputt.value = role.id;
                inputt.type = 'checkbox';
                inputt.name = 'selectedRoles';
                inputt.text = role.name;
                formDelete.appendChild(inputt);
                let xxx = document.createElement("br");
                formDelete.appendChild(xxx);
            });
        })
        .catch(error => console.error(error));
}

//добавление checbox с ролями в модальное окно Edit при загрузке страницы
window.addEventListener("load", loadRolesForDelete);