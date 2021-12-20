function login(){
    var loginFrom = $("#loginForm").validate({
        rules: {
            username: {
                required: true,
                minlength: 4,
                maxlength: 20
            },
            password: {
                required: true,
                minlength: 4,
                maxlength: 15
            }
        },
        messages: {
            username: {
                required: "用户名不能为空",
                minlength: "用户长度不能小于4"
            },
            password: {
                required: "用户密码不能为空",
                minlength: "用户密码长度必须大于4"
            }
        },
        submitHandler: function (form) {
            console.log("登录请求")
            $.post("/user/login", $("#loginForm").serialize(), function (data, status) {
                let result=JSON.parse(data)
                if (result.msg === "登录成功") {
                    window.location.href = "/chat?username="+result.username
                    $('#user_name').text(data.username);
                } else {
                    alert(result.msg);
                }
            });
        }
    });
}

function jumpReg(){
    window.location.href = '/register';
}