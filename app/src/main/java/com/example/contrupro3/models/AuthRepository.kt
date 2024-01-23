package com.example.contrupro3.models

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.contrupro3.models.BudgetModels.Purchases.PurchasesModel
import com.example.contrupro3.models.DocumentsModels.DocumentModel
import com.example.contrupro3.models.ProjectsModels.ProjectModel
import com.example.contrupro3.models.TasksModels.TaskModel
import com.example.contrupro3.models.TeamsModels.TeamMember
import com.example.contrupro3.models.TeamsModels.Teams
import com.example.contrupro3.models.UserModels.NotificationModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class AuthRepository(private val auth: FirebaseAuth) {

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    fun loadProjectsFromFirebase(
        projectsList: MutableState<List<ProjectModel>>,
        onStatusChanged: ((String) -> Unit)? = null
    ) {
        val firestore = FirebaseFirestore.getInstance()
        val user = getCurrentUser()
        if (user != null) {
            val projectsCollection = firestore
                .collection("Users")
                .document(user.uid)
                .collection("Projects")

            projectsCollection.get()
                .addOnSuccessListener { documents ->
                    val loadedProjects = documents.map { document ->
                        val project = document.toObject(ProjectModel::class.java)
                        project.copy(id = document.id)
                    }
                    projectsList.value = loadedProjects
                    onStatusChanged?.invoke("Loaded")
                }
                .addOnFailureListener {
                    onStatusChanged?.invoke("Failed")
                }
        }
    }

    fun loadProject(
        projectId: String,
        project: MutableState<ProjectModel?>,
        onStatusChanged: ((String) -> Unit)? = null
    ) {
        val firestore = FirebaseFirestore.getInstance()
        val user = getCurrentUser()

        if (user != null) {
            val docProject = firestore.collection("Users")
                .document(user.uid)
                .collection("Projects")
                .document(projectId)

            docProject.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val loadedDocument = document.toObject(ProjectModel::class.java)
                        if (loadedDocument != null) {
                            project.value = loadedDocument
                            onStatusChanged?.invoke("Loaded")
                        }
                    } else {
                        project.value = null
                        onStatusChanged?.invoke("Loaded")
                    }
                }.addOnFailureListener {
                    onStatusChanged?.invoke("Failed")
                }
        }
    }

    fun loadEquiposFromFirebase(teamList: MutableState<List<Teams>>, projectId: String) {
        val firestore = FirebaseFirestore.getInstance()
        val user = getCurrentUser()
        if (user != null) {
            val equiposCollection = firestore
                .collection("Users")
                .document(user.uid)
                .collection("Projects")
                .document(projectId)
                .collection("Teams")

            equiposCollection.get()
                .addOnSuccessListener { documents ->
                    val loadedEquipos = documents.map { document ->
                        val equipo = document.toObject(Teams::class.java)
                        equipo.copy(id = document.id)
                    }
                    teamList.value = loadedEquipos
                }
        }
    }

    fun loadTasksFromFirebase(tasksList: MutableState<List<TaskModel>>, projectId: String) {
        val firestore = FirebaseFirestore.getInstance()
        val user = getCurrentUser()
        if (user != null) {
            val tasksCollection = firestore
                .collection("Users")
                .document(user.uid)
                .collection("Projects")
                .document(projectId)
                .collection("Tasks")

            tasksCollection.get()
                .addOnSuccessListener { documents ->
                    val loadedTasks = documents.map { document ->
                        val task = document.toObject(TaskModel::class.java)
                        task.copy(id = document.id)
                    }
                    tasksList.value = loadedTasks
                }
        }
    }

    fun loadTask(
        userId: String,
        projectId: String,
        taskId: String,
        task: MutableState<TaskModel?>,
        onStatusChanged: ((String) -> Unit)? = null
    ) {
        val firestore = FirebaseFirestore.getInstance()
        val user = getCurrentUser()

        if (user != null) {
            val docProject = firestore.collection("Users")
                .document(user.uid)
                .collection("Projects")
                .document(projectId)
                .collection("Tasks")
                .document(taskId)

            docProject.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val loadedDocument = document.toObject(TaskModel::class.java)
                        if (loadedDocument != null) {
                            task.value = loadedDocument
                            onStatusChanged?.invoke("Loaded")
                        }
                    } else {
                        task.value = null
                        onStatusChanged?.invoke("Loaded")
                    }
                }.addOnFailureListener {
                    onStatusChanged?.invoke("Failed")
                }
        }
    }

    fun loadEquipo(
        teamId: String,
        team: MutableState<Teams?>,
        projectId: String,
        onStatusChanged: ((String) -> Unit)? = null
    ) {
        val firestore = FirebaseFirestore.getInstance()
        val user = getCurrentUser()

        if (user != null) {
            val equipoDocument = firestore
                .collection("Users")
                .document(user.uid)
                .collection("Projects")
                .document(projectId)
                .collection("Teams")
                .document(teamId)

            equipoDocument.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val loadedEquipo = document.toObject(Teams::class.java)
                        team.value = loadedEquipo
                        onStatusChanged?.invoke("Loaded")
                    } else {
                        team.value = null
                        onStatusChanged?.invoke("Loaded")
                    }
                }
                .addOnFailureListener { exception ->
                    team.value = null
                    onStatusChanged?.invoke("Failed")
                }
        }
    }

    fun loadMembersOfTeam(
        membersList: MutableState<List<TeamMember>>,
        userId: String,
        projectId: String,
        teamId: String,
        onStatusChanged: ((String) -> Unit)? = null
    ) {
        val firestore = FirebaseFirestore.getInstance()
        val user = getCurrentUser()
        if (user != null) {
            val collection = firestore
                .collection("Users")
                .document(userId)
                .collection("Projects")
                .document(projectId)
                .collection("Teams")
                .document(teamId)
                .collection("Members")

            collection.get()
                .addOnSuccessListener { documents ->
                    val loadedMembers = documents.map { document ->
                        val team = document.toObject(TeamMember::class.java)
                        team.copy(id = document.id)
                    }
                    onStatusChanged?.invoke("Loaded")
                    membersList.value = loadedMembers
                }
                .addOnFailureListener {
                    onStatusChanged?.invoke("Failed")
                }
        }
    }

    fun loadTeamsOfProject(
        teamsList: MutableState<List<Teams>>,
        userId: String,
        projectId: String,
        onStatusChanged: ((String) -> Unit)? = null
    ) {
        val firestore = FirebaseFirestore.getInstance()
        val user = getCurrentUser()
        if (user != null) {
            val collection = firestore
                .collection("Users")
                .document(userId)
                .collection("Projects")
                .document("$projectId")
                .collection("Teams")

            collection.get()
                .addOnSuccessListener { documents ->
                    val loadedTeams = documents.map { document ->
                        val team = document.toObject(Teams::class.java)
                        team.copy(id = document.id)
                    }
                    onStatusChanged?.invoke("Loaded")
                    teamsList.value = loadedTeams
                }
                .addOnFailureListener {
                    onStatusChanged?.invoke("Failed")
                }
        }
    }

    fun loadNotifications(
        notificationsList: MutableState<List<NotificationModel>>,
        onStatusChanged: ((String) -> Unit)? = null
    ) {
        val firestore = FirebaseFirestore.getInstance()
        val user = getCurrentUser()
        if (user != null) {
            val collection = firestore
                .collection("Users")
                .document(user.uid)
                .collection("Notifications")

            collection.get()
                .addOnSuccessListener { documents ->
                    val loadedNotifys = documents.mapNotNull { document ->
                        try {
                            val notify = document.toObject(NotificationModel::class.java)
                            notify?.copy(id = document.id)
                        } catch (e: Exception) {
                            Log.e("ERROR", "Error al convertir el documento", e)
                            onStatusChanged?.invoke("Failed")
                            null
                        }
                    }
                    onStatusChanged?.invoke("Loaded")
                    notificationsList.value = loadedNotifys
                }
                .addOnFailureListener {
                    onStatusChanged?.invoke("Failed")
                }
        }
    }

    fun loadDocumentsFromFirebase(
        projectId: String,
        documentsList: MutableState<List<DocumentModel>>
    ) {
        val firestore = FirebaseFirestore.getInstance()
        val user = getCurrentUser()
        if (user != null) {
            val documentsCollection = firestore
                .collection("Users")
                .document(user.uid)
                .collection("Projects")
                .document(projectId)
                .collection("Documents")

            documentsCollection.get()
                .addOnSuccessListener { documents ->
                    val loadedDocs = documents.map { document ->
                        val doc = document.toObject(DocumentModel::class.java)
                        doc.copy(id = document.id)
                    }
                    documentsList.value = loadedDocs // Actualiza el valor del MutableState aquí
                }
                .addOnFailureListener { exception ->
                    // Manejar el error en caso de que la carga de equipos falle
                }
        }
    }

    fun loadDocument(documentoId: String, documento: MutableState<DocumentModel?>) {
        val firestore = FirebaseFirestore.getInstance()
        val user = getCurrentUser()

        if (user != null) {
            val docDocument = firestore
                .collection("Users")
                .document(user.uid)
                .collection("Documents")
                .document(documentoId)

            docDocument.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val loadedDocument = document.toObject(DocumentModel::class.java)
                        documento.value = loadedDocument
                    } else {
                        documento.value = null
                    }
                }
                .addOnFailureListener { exception ->
                    documento.value = null
                }
        }
    }

    fun firebaseAuthWithGoogle(idToken: String, onSuccess: () -> Unit, onFail: (String) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onFail(task.exception?.message ?: "Error desconocido")
                }
            }
    }

    fun loadPurchasesFromFirebase(
        projectId: String,
        purchasesList: MutableState<List<PurchasesModel>>
    ) {
        val firestore = FirebaseFirestore.getInstance()
        val user = getCurrentUser()

        if (user != null && projectId != null) {
            val budgetsCollection = firestore
                .collection("Users")
                .document(user.uid)
                .collection("Projects")
                .document(projectId)
                .collection("Purchases")

            budgetsCollection.get()
                .addOnSuccessListener { documents ->
                    val loadedPurchases = documents.map { document ->
                        val purchase = document.toObject(PurchasesModel::class.java)
                        purchase.copy(id = document.id)
                    }
                    purchasesList.value = loadedPurchases
                }
        }
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun getLoggedInUserUID(): LiveData<String> {
        val user = FirebaseAuth.getInstance().currentUser
        return MutableLiveData(user?.uid ?: "")
    }

    fun getLoggedInUserName(): LiveData<String> {
        val userNameLiveData = MutableLiveData<String>()
        val user = auth.currentUser
        if (user != null) {
            val firestore = FirebaseFirestore.getInstance()
            val docRef: DocumentReference = firestore.collection("Users")
                .document(user.uid)

            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val name = document.getString("name") ?: "Nombre desconocido"
                        val lastName = document.getString("lastName") ?: "Apellido desconocido"
                        val fullName = "${name.capitalize()} ${lastName.capitalize()}"
                        val truncatedFullName = if (fullName.length > 18) {
                            fullName.substring(0, 18) + "..."
                        } else fullName

                        userNameLiveData.value = truncatedFullName
                    } else userNameLiveData.value = "Usuario no autenticado"
                }
                .addOnFailureListener { exception ->
                    userNameLiveData.value = "Error al obtener el nombre del usuario"
                    Log.e("MyApp", "Error al obtener el nombre del usuario", exception)
                }
        } else userNameLiveData.value = "Usuario no autenticado"
        return userNameLiveData
    }

    fun getAuth(): FirebaseAuth {
        return auth
    }

    fun isEmailVerified(): Boolean {
        val user = auth.currentUser
        return user?.isEmailVerified ?: false
    }

    fun logoutUser(onComplete: (Boolean) -> Unit) {
        auth.signOut()
        onComplete(auth.currentUser == null) // Invocará onComplete con true si el usuario se desconectó correctamente
    }

    fun resetPassword(email: String, onSuccess: () -> Unit, onFail: () -> Unit) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess()
            } else {
                onFail()
            }
        }
    }
}