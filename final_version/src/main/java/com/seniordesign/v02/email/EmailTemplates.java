package com.seniordesign.v02.email;

public class EmailTemplates {
    private String name;
    private String token;
    private String password;

    public EmailTemplates(String name, String token){
            this.name=name;
            this.token=token;
            }
    public EmailTemplates(String password) {
        this.password = password;
    }
            public String buildRegistrationEmail(){
                return "<html>\n" +
                        "  <head>\n" +
                        "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                        "    <style>\n" +
                        "    </style>\n" +
                        "  </head>\n" +
                        "  <body class=\"bg-light\">\n" +
                        "    <div class=\"container\">\n" +
                        "      <div class=\"card my-10\">\n" +
                        "        <div class=\"card-body\">\n" +
                        "          <h1 class=\"text-teal-700\">Confirm your email</h1>\n" +
                        "          <hr>\n" +
                        "          <div class=\"space-y-3\">\n" +
                        "            <p class=\"text-gray-700\">\n" +
                        "              Hi " + name + "\n" + "," +
                        "            </p>\n" +
                        "              <p class=\"text-gray-700\">\n" +
                        "              Thank you for registering. Please paste the token below into the verification box to activate your account:\n" +
                        "            </p>\n" +
                        "              <p class=\"text-gray-700\">\n" +
                        "              Token :  " + token + "\n" +
                        "            </p>\n" +
                        "            <p class=\"text-gray-700\">\n" +
                        "              Best regards,\n" +
                        "            </p>\n" +
                        "            <p class=\"text-gray-700\">\n" +
                        "              myNotesApp support\n" +
                        "            </p>\n" +
                        "\n" +
                        "      </div>\n" +
                        "    \t</div>\n" +
                        "\t\t</div>\n" +
                        "    </div>\n" +
                        "  </body>\n" +
                        "</html>\n";
            }
            public String buildForgotPasswordEmail(){
                return "<html>\n" +
                        "  <head>\n" +
                        "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                        "    <style>\n" +
                        "    </style>\n" +
                        "  </head>\n" +
                        "  <body class=\"bg-light\">\n" +
                        "    <div class=\"container\">\n" +
                        "      <div class=\"card my-10\">\n" +
                        "        <div class=\"card-body\">\n" +
                        "          <h1 class=\"text-teal-700\">Auto-generated password</h1>\n" +
                        "          <hr>\n" +
                        "          <div class=\"space-y-3\">\n" +
                        "            <p class=\"text-gray-700\">\n" +
                        "              Hello, \n" +
                        "            </p>\n" +
                        "              <p class=\"text-gray-700\">\n" +
                        "              You can use the password below to access your account:\n" +
                        "            </p>\n" +
                        "              <p class=\"text-gray-700\">\n" +
                        "              New Password :  " + password + "\n" +
                        "            </p>\n" +
                        "            <p class=\"text-gray-700\">\n" +
                        "              Best regards,\n" +
                        "            </p>\n" +
                        "            <p class=\"text-gray-700\">\n" +
                        "              myNotesApp support\n" +
                        "            </p>\n" +
                        "\n" +
                        "      </div>\n" +
                        "    \t</div>\n" +
                        "\t\t</div>\n" +
                        "    </div>\n" +
                        "  </body>\n" +
                        "</html>\n";
            }

}
