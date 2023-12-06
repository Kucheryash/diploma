//package project.configs;
//
//import org.springframework.stereotype.Component;
//
//@Component
//public class CustomAuthentication extends SimpleUrlAuthenticationSuccessHandler {
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        User user = (User) authentication.getPrincipal();
//        Role role = user.getRole();
//
//        if (role == Role.ROLE_SPEC)
//            response.sendRedirect("/specialist");
//        else {
//            String userId = String.valueOf(user.getId());
//            response.sendRedirect("/home/" + userId);
//        }
//    }
//}
