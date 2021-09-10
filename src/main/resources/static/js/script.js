function deleteContact(id) {
	swal({
		title: "Are you sure you want to delete?",
		text: "Once deleted can not be recovered",
		icon: "warning",
		buttons: true,
		dangerMode: true,
	})
		.then((willDelete) => {
			if (willDelete) {
				window.location = "/user/contact/delete/" + id;
			}
		});
}

function search() {

	var query = $("#search").val();

	if (query == '') {
		$(".search-result").hide();
	} else {

		let url = `http://localhost:8080/user/search/${query}`;
		fetch(url).then(response => {
			return response.json();
		}).then(data => {

			let text = `<div class="btn-group">`
			data.forEach(contact => {
				text += `<a href="/user/contact/${contact.id}" class="btn btn - light" data-mdb-color="dark">${contact.name}</a>`
			});

			text += `</div>`

			$(".search-result").html(text);

			$(".search-result").show();
		});


	}

}









// confirm passwords

function confirmPasswords() {
	var pass = $("#pass").val();

	var confPass = $("#conf-pass").val();

	if (confPass == pass) {
		$("#verify-btn").removeAttr("disabled");

	} else {
		$("#verify-btn").attr("disabled", true);
	}

}