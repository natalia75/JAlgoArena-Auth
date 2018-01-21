package com.jalgoarena.EmailSender

import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.HtmlEmail
import java.net.URL

class EmailSender(psenderEmail: String,psenderPassword: String) {
    val senderEmail = psenderEmail
    val senderPassword = psenderPassword


    fun SendEmail(userEmail: String, problemName: String){
        val email = HtmlEmail()
        try{
            email.hostName = "smtp.gmail.com"
            email.setSmtpPort(465)
            email.setAuthenticator(DefaultAuthenticator(senderEmail, senderPassword))
            email.isSSLOnConnect=true
            email.setFrom(senderEmail)
            email.addTo(userEmail)
            email.subject = "Propozycja problemu do rozwiazania"
            email.setHtmlMsg("<HTML><h1>${problemName}</HTML>")
            email.send()
        }catch(e: Exception){
            println(e.message)
        }

    }


}