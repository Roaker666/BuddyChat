function register(){
    let registerForm = $("#RegisterForm").validate({
        rules : {
            username : {
                required : true,
                minlength : 4,
                maxlength : 20
            },
            password : {
                required: true,
                minlength: 4,
                maxlength: 15
            },
        },
        messages : {
            username : {
                required : "用户名不能为空",
                minlength : "用户长度不能小于4"
            },
            password: {
                required : "用户密码不能为空",
                minlength : "用户密码长度必须大于4"
            }
        },
        submitHandler : function(form){
            $.post("/user/register",$("#RegisterForm").serialize(),function (data,status){
                if (data === "注册成功") {
                    window.location.href = "/";
                } else {
                    alert("注册失败，请重新注册")
                }
            });
        }
    });
}