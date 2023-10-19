async function getUserById(id) {
    let response = await fetch("http://localhost:8080/api/admin/" + id);
    return await response.json();
}
//let rolesForEdit = [];
//заполнение модального окна
async function open_fill_modal(form, modal, id) {
    modal.show();
    let user = await getUserById(id);
    console.log("userBy_id = " + user);
    form.reset();
    form.id.value = user.id;
    form.firstName.value = user.firstName;
    form.lastName.value = user.lastName;
    form.email.value = user.email;
    form.password.value = '';
    console.log(user.roles);
    let nameInputs = form.querySelectorAll("[name='selectedRoles']");
    for (let i = 0; i < nameInputs.length; i++) {
        for(let j = 0; j < user.roles.length;j++)
        if (nameInputs[i].id === 'role_Edit_' + user.roles[j].id) {
            nameInputs[i].checked = true;
            //rolesForEdit.push(user.roles[j]);
        }
    }
}

