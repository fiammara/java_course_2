package com.example.library3.model;




public class User {

        private Long user_id;
        private String userName;
        private String password;
        private String email;

        public Long getId() {
                return user_id;
        }

        public void setId(Long id) {
                this.user_id = id;
        }

        public String getUserName() {
                return userName;
        }

        public void setUserName(String userName) {
                this.userName = userName;
        }

        public String getPassword() {
                return password;
        }

        public void setPassword(String password) {
                this.password = password;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }


        @Override
        public String toString() {
                return "User{" +
                        "id=" + user_id +
                        ", userName='" + userName + '\'' +
                        ", password='" + password + '\'' +
                        ", email='" + email + '\'' +
                        '}';
        }
}
