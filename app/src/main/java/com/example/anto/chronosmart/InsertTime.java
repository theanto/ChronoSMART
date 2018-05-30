package com.example.anto.chronosmart;

public class InsertTime {
    
        private String userID;
        private String timestamp;
        private String timerun;
    

        public InsertTime(String userID, String timestamp, String timerun) {
            this.userID = userID;
            this.timestamp = timestamp;
            this.timerun = timerun;
        }

       public String gettimestamp() {
            return timestamp;
        }

        public void settimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getuserID() {
            return userID;
        }

        public void setuserID(String userID) {
            this.userID = userID;
        }

        public String gettimerun() {
            return timerun;
        }

        public void settimerun(String timerun) {
            this.timerun = timerun;
        }
    
}
