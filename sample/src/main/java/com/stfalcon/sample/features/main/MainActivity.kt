package com.stfalcon.sample.features.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.stfalcon.sample.R
import com.stfalcon.sample.features.demo.grid.PostersGridDemoActivity
import com.stfalcon.sample.features.demo.rotation.RotationDemoActivity
import com.stfalcon.sample.features.demo.scroll.ScrollingImagesDemoActivity
import com.stfalcon.sample.features.demo.styled.StylingDemoActivity
import com.stfalcon.sample.features.main.adapter.MainActivityPagerAdapter
import com.stfalcon.sample.features.main.adapter.MainActivityPagerAdapter.Companion.ID_IMAGES_GRID
import com.stfalcon.sample.features.main.adapter.MainActivityPagerAdapter.Companion.ID_ROTATION
import com.stfalcon.sample.features.main.adapter.MainActivityPagerAdapter.Companion.ID_SCROLL
import com.stfalcon.sample.features.main.adapter.MainActivityPagerAdapter.Companion.ID_STYLING
import com.stfalcon.sample.features.main.card.DemoCardFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(),
        DemoCardFragment.OnCardActionListener {

    var GLOBE_PATH_ROOT = ""

    fun getExternalSdCardPath(): String {
        var path: String? = null

        var sdCardFile: File? = null
        val sdCardPossiblePath = Arrays.asList("external_sd", "ext_sd", "external", "extSdCard")

        for (sdPath in sdCardPossiblePath) {
            val file = File("/storage/", sdPath)

            if (file.isDirectory && file.canWrite()) {
                path = file.absolutePath

                val timeStamp = SimpleDateFormat("ddMMyyyy_HHmmss").format(Date())
                val testWritable = File(path, "test_$timeStamp")

                if (testWritable.mkdirs()) {
                    testWritable.delete()
                } else {
                    path = null
                }
            }
        }

        if (path != null) {
            sdCardFile = File(path)
        } else {
            sdCardFile = File(Environment.getExternalStorageDirectory().absolutePath)
        }

        return sdCardFile.absolutePath
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainCardsViewPager.apply {
            adapter = MainActivityPagerAdapter(this@MainActivity, supportFragmentManager)
            pageMargin = resources.getDimension(R.dimen.card_padding).toInt() / 4
            offscreenPageLimit = 3
        }
        mainCardsPagerIndicator.setViewPager(mainCardsViewPager)
        if (Build.VERSION.SDK_INT >= 29) {
            var exsdYes = "exSD card NOT able !!!"
            val state = Environment.getExternalStorageState()
            if (Environment.MEDIA_MOUNTED == state) {
                exsdYes = "可以讀寫"
                var mutableList: MutableList<String> = mutableListOf()
                var mutableList2: MutableList<String> = mutableListOf()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    val files = getExternalFilesDirs(Environment.MEDIA_MOUNTED)
                    for (file in files) {
                        if (file.absolutePath.toString().contains("emulated/0", ignoreCase = true)) {
                            mutableList2.add(file.absolutePath)
                        } else {
                            exsdYes += "file_dir" + file.absolutePath
                            mutableList.add(file.absolutePath)
                        }
                    }
                }
                if (mutableList.size != 0) {
                    exsdYes = mutableList.get(0)
                    GLOBE_PATH_ROOT = "/" + mutableList.get(0).split('/')[1] + "/" + mutableList.get(0).split('/')[2]
                    if (!Files.notExists(Paths.get(GLOBE_PATH_ROOT))) {

                        val toast = Toast.makeText(applicationContext, "GLOBE_PATH_ROOT--->" + GLOBE_PATH_ROOT, Toast.LENGTH_LONG)
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
                        toast.show()
                        mainTitleText.text=GLOBE_PATH_ROOT
                    } else {

                        GLOBE_PATH_ROOT = getExternalSdCardPath()
                        if (!Files.notExists(Paths.get(GLOBE_PATH_ROOT))) {

                            val toast = Toast.makeText(applicationContext, "GLOBE_PATH_ROOT--->" + GLOBE_PATH_ROOT, Toast.LENGTH_LONG)
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
                            toast.show()
                            mainTitleText.text=GLOBE_PATH_ROOT

                        } else {

                            val toast = Toast.makeText(applicationContext, GLOBE_PATH_ROOT + "--->notExists (External memory card address access error)", Toast.LENGTH_LONG)
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
                            toast.show()
                            mainTitleText.text=GLOBE_PATH_ROOT

                        }
                    }
                } else {
                    GLOBE_PATH_ROOT = getExternalSdCardPath()
                    if (!Files.notExists(Paths.get(GLOBE_PATH_ROOT))) {

                        val toast = Toast.makeText(applicationContext, "GLOBE_PATH_ROOT--->" + GLOBE_PATH_ROOT, Toast.LENGTH_LONG)
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
                        toast.show()
                        mainTitleText.text=GLOBE_PATH_ROOT

                    } else {

                        val toast = Toast.makeText(applicationContext, GLOBE_PATH_ROOT + "--->notExists", Toast.LENGTH_LONG)
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
                        toast.show()
                        mainTitleText.text=GLOBE_PATH_ROOT

                    }
                }
            } else if (Environment.MEDIA_MOUNTED_READ_ONLY == state) {
                exsdYes = "只可以讀取，無法寫入"
                if (Environment.isExternalStorageLegacy()) {
                    exsdYes += "exSD card able"
                }
                val toast = Toast.makeText(applicationContext, exsdYes/*getExternalFilesDir(null).toString()*/, Toast.LENGTH_LONG)
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
                toast.show()
            } else {
                exsdYes = "無法讀寫"
                if (Environment.isExternalStorageLegacy()) {
                    exsdYes += "exSD card able"
                }
                val toast = Toast.makeText(applicationContext, exsdYes/*getExternalFilesDir(null).toString()*/, Toast.LENGTH_LONG)
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
                toast.show()
            }


            val fileName = "/storage/0046-E805/20210511_025841.jpg"
            var file = File(fileName)
            var fileExists = file.exists()

            if (fileExists) {
                exsdYes += "$fileName does exist."
            } else {
                exsdYes += "$fileName does not exist."
            }


        } else {
            val toast = Toast.makeText(applicationContext, "android version too old, need android 10 up ...", Toast.LENGTH_LONG)
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
            toast.show()
        }

    }

    override fun onCardAction(actionId: Int) {
        when (actionId) {
            ID_IMAGES_GRID -> {
                startActivity(Intent(this, PostersGridDemoActivity::class.java))
            }
            ID_SCROLL -> {
                startActivity(Intent(this, ScrollingImagesDemoActivity::class.java))
            }
            ID_STYLING -> {
                startActivity(Intent(this, StylingDemoActivity::class.java))
            }
            ID_ROTATION -> {
                startActivity(Intent(this, RotationDemoActivity::class.java))
            }
        }
    }
}
