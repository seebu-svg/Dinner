package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        DinnerEventEntity::class,
        RestaurantEntity::class,
        HostEntity::class,
        BookingEntity::class,
        CollaborationProposalEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class SocialDiningDatabase : RoomDatabase() {
    abstract fun dao(): SocialDiningDao

    companion object {
        @Volatile
        private var INSTANCE: SocialDiningDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): SocialDiningDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SocialDiningDatabase::class.java,
                    "social_dining_marketplace.db"
                ).addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        INSTANCE?.let { database ->
                            scope.launch(Dispatchers.IO) {
                                InitialData.populateInitialData(database.dao())
                            }
                        }
                    }
                }).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
