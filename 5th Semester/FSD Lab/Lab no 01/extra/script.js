const title = document.querySelection("p");
const button = document.querySelector("#likebtn");
const message = document.querySelector("#message");

button.addEventListener("click", function () {
    title.textContent = "Updated content";
    message.textContent = "You liked your profile";
});

