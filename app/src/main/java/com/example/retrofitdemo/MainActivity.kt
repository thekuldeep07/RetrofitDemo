package com.example.retrofitdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private  lateinit var retService: AlbumService
    private lateinit var tv:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        retService = RetrofitInstance.getRetrofitInstance().create(AlbumService::class.java)

        tv = findViewById<TextView>(R.id.textView)

        //getRequestWithQueryParameters()
//        getRequestWithPathParameters()


        uploadAlbum()







    }

    private  fun getRequestWithQueryParameters(){
        val responseLiveData:LiveData<Response<Albums>> = liveData {
//            val response = retService.getAlbums()
            val response = retService.getSortedAlbums(3)
            emit(response)
        }
        responseLiveData.observe(this, Observer {
            val albumsList : MutableListIterator<AlbumsItem>? = it.body()?.listIterator()
            if(albumsList!=null){
                while (albumsList.hasNext()){
                    val albumsItem :AlbumsItem = albumsList.next()
                    val result:String = " "+ "Album id : ${albumsItem.id}" + "\n"+
                            " "+ "Album Title : ${albumsItem.title}"+"\n"+
                            " "+"User id : ${albumsItem.userId}"+"\n\n\n"

                    tv.append(result)


                }
            }
        })

    }

    private  fun getRequestWithPathParameters(){
        val pathResponse:LiveData<Response<AlbumsItem>> = liveData {
            val response = retService.getAlbum(3)
            emit(response)
        }

        pathResponse.observe(this, Observer {
            val title:String?=it.body()?.title

        })
    }


    private fun uploadAlbum(){
        val album =AlbumsItem(999,"My Title",123)
        val postResponse : LiveData<Response<AlbumsItem>> = liveData {
            val response :Response<AlbumsItem> = retService.uploadAlbum(album)
            emit(response)
        }

        postResponse.observe(this, Observer {
            val albumsItem:AlbumsItem? =  it.body()
            val result:String = " "+ "Album id : ${albumsItem?.id}" + "\n"+
                    " "+ "Album Title : ${albumsItem?.title}"+"\n"+
                    " "+"User id : ${albumsItem?.userId}"+"\n\n\n"

            tv.text = result

        })
    }


}