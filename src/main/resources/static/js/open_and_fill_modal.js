async function getUserById(id) {
    let response = await fetch("http://localhost:8080/api/admin/" + id);
    return await response.json();
}

//заполнение модального окна
async function open_fill_modal(form, modal, id) {
    modal.show();
    let userModal = await getUserById(id);
    console.log("userBy_id = " + userModal);
    form.reset();
    form.id.value = userModal.id;
    form.firstName.value = userModal.firstName;
    form.lastName.value = userModal.lastName;
    form.email.value = userModal.email;
    console.log(userModal.roles);

}

