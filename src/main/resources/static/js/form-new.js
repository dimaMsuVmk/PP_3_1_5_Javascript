let formCreate = document.forms["formNew"];

createNewUser();

function createNewUser() {
    formCreate.addEventListener("submit", ev => {
        ev.preventDefault();
        let nameInputs = formCreate.querySelectorAll("[name='selectedRoles']");
        let rolesForCreate = [];
        for (let i = 0; i < nameInputs.length; i++) {
            if (nameInputs[i].checked) rolesForCreate.push({
                id: nameInputs[i].value,
                name: nameInputs[i].text
            });
        }

        fetch("http://localhost:8080/api/admin/", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                id: formCreate.id.value,
                firstName: formCreate.firstName.value,
                lastName: formCreate.lastName.value,
                email: formCreate.email.value,
                password: formCreate.password.value,
                roles: rolesForCreate,
            })
        }).then(() => {
            formCreate.reset();
            fetch(URLEdit)
                .then(response => response.json())
                .then(arrayUsers => fillTable(arrayUsers))
                .catch(error => console.log(error));
            $('#usersTable').click(); //клик по кнопке Users Table
        });
    });
}

//Приведение загруженных ролей в формате java к виду JS. Их просто грузим сразу, как появляется форма
function loadRolesForCreate() {
    let formCreate = document.getElementById("div_create");
    fetch("http://localhost:8080/api/roles")
        .then(res => res.json())
        .then(list_Roles => {
            list_Roles.forEach(role => {
                let labell = document.createElement("label");
                labell.for = 'role_' + role.id;
                labell.innerText = role.name + ' ';
                formCreate.appendChild(labell);
                let inputt = document.createElement("input");
                inputt.id = 'role_' + role.id;
                inputt.value = role.id;
                inputt.type = 'checkbox';
                inputt.name = 'selectedRoles';
                inputt.text = role.name;
                formCreate.appendChild(inputt);
                let xxx = document.createElement("br");
                formCreate.appendChild(xxx);
            });
        })
        .catch(error => console.error(error));
}

window.addEventListener("load", loadRolesForCreate);