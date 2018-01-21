package com.jalgoarena.EmailSender

import com.google.gson.JsonObject
import com.jalgoarena.data.UsersRepository
import com.jalgoarena.data.XodusUsersRepository
import com.jalgoarena.domain.User
//import com.jalgoarena.web.HttpUsersClient
import java.time.LocalDateTime
//import com.jalgoarena.web.UsersClient;
import com.netflix.discovery.EurekaClient
import com.sun.xml.internal.ws.resources.DispatchMessages
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import javax.inject.Inject

//class EmailSenderThread(pStartDate: LocalDateTime, pSendPeriodInSeconds : Long, @Inject private val repository: UsersRepository) : Thread(){
class EmailSenderThread : Thread(){
    val limit = 50
    var startDate = LocalDateTime.now()
    var sendPeriodInSeconds = 10
    var lastSendDate = startDate
    //var repo = XodusUsersRepository()
    var mailSender: EmailSender = EmailSender("probnymail15@gmail.com","probnymail1")


    public fun run(repository: XodusUsersRepository){
        val repo = repository
        println("New thread started: ${Thread.currentThread()} | ${startDate}")
        var i = 0
        while(true) {
            SendMail(lastSendDate, sendPeriodInSeconds, repo)
            logInfo(0)
            Thread.sleep(1000)
        }
    }

    private fun FindAllUsers(repo: XodusUsersRepository): List<User>{
        val usersList = repo.findAll()
        println(">>> ${usersList[0].email}")
        return usersList
    }

    private fun GetAllProblems(): String{
        var problemsAsJSON: String = "empty"
        val url = "http://localhost:5002/problems"

        val obj = URL(url)

        with(obj.openConnection() as HttpURLConnection){
            requestMethod = "GET"
            println("Response code: ${responseCode}")

            problemsAsJSON = obj.readText()
            //println("AAA: ${result}")
        }
        return problemsAsJSON
    }

    private fun getProblemsTitleFromJSON(json: String): List<String>{
        var startIdx = 0
        var endIdx = 0
        var start = 0
        var resultList: MutableList<String> = mutableListOf()
        startIdx = json.indexOf("\"title\":",start)
        startIdx += 9
        endIdx = json.indexOf("\"",startIdx+1)
        println("Startidx: ${startIdx} | Endinx: ${endIdx}")

        while(startIdx>0){
            startIdx += 9
            endIdx = json.indexOf("\"",startIdx+1)
            var firstProblem = json.substring(startIdx,endIdx)
            println(firstProblem)
            resultList.add(firstProblem)
            start = endIdx
            startIdx = json.indexOf("\"title\":",start)
        }
        return resultList
    }

    private fun <E> List<E>.getRandomElement() = this[Random().nextInt(this.size)]

    private fun SendMail(pLastSendDate: LocalDateTime, pSendPeriodInSeconds: Int, repo: XodusUsersRepository){
        var currentDate = LocalDateTime.now()
        var dateMinusOneDay = currentDate.minusSeconds(pSendPeriodInSeconds.toLong())
        if(dateMinusOneDay > pLastSendDate){
            //TODO: Send mail
            println(">>>> Mail will be send <<<<<")
            lastSendDate = currentDate

            try{
                var users = FindAllUsers(repo)
                var problemsToParse = GetAllProblems()
                var problems = getProblemsTitleFromJSON(problemsToParse)
                var problemToSend = problems.getRandomElement()
                mailSender.SendEmail("probnymail15@gmail.com",problemToSend)
            }catch(e: Exception){
                println(e.message)
            }

        }
    }

    private fun logInfo(i: Int){
        println(">>>>> I'm working... : ${i}")
    }


}