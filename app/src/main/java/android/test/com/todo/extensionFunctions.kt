package android.test.com.todo

import android.support.annotation.LayoutRes
import android.test.com.todo.utils.DB_NAME
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.FirebaseFirestore

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View =
        LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)

fun getDbInsatance() = FirebaseFirestore.getInstance().collection(DB_NAME)
