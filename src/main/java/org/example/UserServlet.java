package org.example;

//public class UserServlet extends HttpServlet {
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        List<User> users = CRUDUtils.getAllUsers();
//        response.setContentType("text/html");
//        response.getWriter().println("Users:");
//        for (User user : users) {
//            response.getWriter().println(user.getUser_id() + " - " + user.getLogin());
//        }
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        int user_id= Integer.parseInt(request.getParameter("user_id"));
//        String login = request.getParameter("login");
//        String password = request.getParameter("password");
//        User newUser = new User(user_id,login,password);
//        CRUDUtils.saveUser(newUser);
//        response.getWriter().println("User created successfully!");
//    }
//}
