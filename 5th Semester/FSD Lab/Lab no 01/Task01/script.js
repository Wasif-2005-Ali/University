$(document).ready(function () {


    $('.push-btn').click(function () {

        let value = $(this).text();


        let newItem = $('<div class="stack-item"></div>').text(value);


        $('#stack-container').prepend(newItem);
    });


    $('#stack-container').on('click', '.stack-item', function () {


        if ($(this).is(':first-child')) {
            $(this).remove();
        } else {

            alert("Invalid POP! In a Stack, you can only pop the top-most element.");
        }
    });

});