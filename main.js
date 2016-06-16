$(document).ready(function() {
    doYouLikeMeme.init();
})

var doYouLikeMeme = {
  init: function() {
    doYouLikeMeme.styling();
    doYouLikeMeme.events();
  },//end init funciton
  styling: function() {
    doYouLikeMeme.get();
  },
  events: function() {

  },


        /////AJAX/////

  create: function(thingToDo) {
  $.ajax({
      url: gabeToDo.url,
      method: "POST",
      data: thingToDo,
      success: function(data) {
          console.log("works", data);
          gabeToDo.todos.push(data);
          gabeToDo.getToDo();
      },
      error: function(err) {
          console.error("OH CRAP", err);
      }
    })
          },

        get: function() {
            $.ajax({
                // url:
                method: "GET",
                success: function(data) {
                    console.log("WE GOT SOMETHING", data);
                    $(".tasksremaining").find('h5').text("Tasks Remaining: " + data.length); ///this counts the list items
                    $('ul').html("");
                    data.forEach(function(element, idx) {
                        var toDoStr = `<li data-id="${element._id}">${element.todo}<a href=""> ✓</a></li>`
                        $('ul').append(toDoStr)
                        gabeToDo.todos.push(element);
                    });
                },
                error: function(err) {
                    console.error("ugh", err);
                }
            })
        },
        delete: function(toDoId) {
            // find post to delete from our post data;
            var DeletePost = gabeToDo.url + "/" + toDoId;
            $.ajax({
                url: DeletePost,
                method: "DELETE",
                success: function(data) {
                    console.log("WE DELETED SOMETHING", data);
                    gabeToDo.getToDo();

                },
                error: function(err) {
                    console.error("ugh", err);
                }
            })
        },

        edit: function(thingToEdit) {
            console.log("THING TO EDIT", thingToEdit);
            $.ajax({
                url: gabeToDo.url + "/" + thingToEdit._id,
                method: "PUT",
                data: thingToEdit,
                success: function(data) {
                    console.log("edited!!!!!", data)
                    gabeToDo.getToDo();
                },
                error: function(err) {
                    console.error("OH CRAP", err);
                }
            })
        }
      };
