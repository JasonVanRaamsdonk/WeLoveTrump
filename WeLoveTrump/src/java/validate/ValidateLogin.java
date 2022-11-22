/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package validate;

import dbconnection.DBConnect;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author stephen
 */
public class ValidateLogin extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getParameter("username").trim();
        String password = request.getParameter("password").trim();

        String sqlQuery = "select * from users where username = ? and password = ?";
        PreparedStatement statement = null;
        
        try
             {
                 Connection con=new DBConnect().connect(getServletContext().getRealPath("/WEB-INF/config.properties"));
                    if(con!=null && !con.isClosed())
                               {
                                    statement = con.prepareStatement(sqlQuery);
                                    statement.setString(1, username);
                                    statement.setString(2, password);
                                    ResultSet resultSet = statement.executeQuery();
                                //    ResultSet rs = null;
                                //    Statement statement = con.createStatement();  
                                //    rs = statement.executeQuery("select * from users where username='"+user+"' and password='"+pass+"'");
                                   if(resultSet != null && resultSet.next()){
                                        HttpSession session=request.getSession();
                                        session.setAttribute("userid", resultSet.getString("id"));
                                        session.setAttribute("user", resultSet.getString("username"));
                                        session.setAttribute("isLoggedIn", "1");
                                        String cookie = "privilege="+randomCookie();
                                        response.addHeader("Set-Cookie", cookie+"; HttpOnly; Secure; SameSite=strict");
                                        response.setHeader("Access-Control-Allow-Credentials", "false");
                                        response.setHeader("Content-Security-Policy", "self");
                                        response.setHeader("Access-Control-Allow-Headers","Origin, X-Requested-With, Content-Type, Accept, X-Auth-Token, X-Csrf-Token, WWW-Authenticate, Authorization");
                                        response.sendRedirect("members.jsp");
                                   }
                                    
                               }
                }
               catch(Exception ex)
                {
                           response.sendRedirect("login.jsp");
                 }
        
        
    }
    
//     private String getMD5(String user) {

//         MessageDigest mdAlgorithm = null;
//         try {
//             mdAlgorithm = MessageDigest.getInstance("MD5");
//         } catch (NoSuchAlgorithmException ex) {
//             Logger.getLogger(ValidateLogin.class.getName()).log(Level.SEVERE, null, ex);
//         }
//         mdAlgorithm.update(user.getBytes());

//         byte[] digest = mdAlgorithm.digest();
//         StringBuffer hexString = new StringBuffer();

//         for (int i = 0; i < digest.length; i++) {
//             user = Integer.toHexString(0xFF & digest[i]);

//             if (user.length() < 2) {
//                 user = "0" + user;
//             }

//             hexString.append(user);
//         }

// return hexString.toString();

    private static String randomCookie() {
        byte[] byteArray = new byte[20];
        new SecureRandom().nextBytes(byteArray);
        return bytesToHex(byteArray);
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte temp : bytes) {
            result.append(String.format("%02x", temp));
        }
        return result.toString();
    }
    // public static void convertByteToHexadecimal(byte[] byteArray)
    // {
    //     String hex = "";
  
    //     // Iterating through each byte in the array
    //     for (byte i : byteArray) {
    //         hex += String.format("%02X", i);
    //     }
  
    //     return hex;
    // }

        
//     }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
