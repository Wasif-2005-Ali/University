$(document).ready(function () {

    $('.enq-btn').click(function () {

        let value = $(this).text();


        let newItem = $('<div class="queue-item"></div>').text(value);


        $('#queue-container').append(newItem);
    });


    $('#queue-container').on('click', '.queue-item', function () {

        if ($(this).is(':first-child')) {
            $(this).remove();
        } else {
            alert("Invalid DEQUEUE! In a Queue, you can only remove the element at the front.");
        }
    });

});