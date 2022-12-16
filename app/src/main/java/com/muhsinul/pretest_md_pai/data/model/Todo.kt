package com.muhsinul.pretest_md_pai.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "todo")
data class Todo (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "title")
    var title: String?,
    @ColumnInfo(name = "desc")
    var desc: String?,
    @ColumnInfo(name = "is_task_completed")
    var isTaskCompleted: Boolean = false
) : Parcelable