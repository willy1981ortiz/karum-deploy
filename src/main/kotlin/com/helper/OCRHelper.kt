package com.helper

import com.google.cloud.vision.v1.*
import com.google.protobuf.ByteString
import java.io.File
import java.io.FileInputStream


object OCRHelper {
    fun extract(file: File): MutableList<EntityAnnotation> {
        return detectText(file.path)
    }

    // Detects text in the specified image.
    fun detectText(filePath: String?): MutableList<EntityAnnotation> {
        val entityAnnotations = mutableListOf<EntityAnnotation>()
        val requests: MutableList<AnnotateImageRequest> = ArrayList()
        val imgBytes: ByteString = ByteString.readFrom(FileInputStream(filePath))
        val img: Image = Image.newBuilder().setContent(imgBytes).build()
        val feat: Feature = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build()

        val request: AnnotateImageRequest = AnnotateImageRequest.newBuilder()
            .addFeatures(feat).setImage(img)
            .build()
        requests.add(request)
        ImageAnnotatorClient.create().use { client ->
            val response: BatchAnnotateImagesResponse = client.batchAnnotateImages(requests)
            val responses: List<AnnotateImageResponse> = response.responsesList
            for (res in responses) {
                if (res.hasError()) {
                    System.out.format("Error: %s%n", res.error.message)
                    return entityAnnotations
                }

                // For full list of available annotations, see http://g.co/cloud/vision/docs
                entityAnnotations.addAll(res.textAnnotationsList)
               /* for (annotation in res.textAnnotationsList) {
                    System.out.format("Text: %s%n", annotation.description)
                    System.out.format("Position : %s%n", annotation.boundingPoly)
                }*/
            }
        }

        return entityAnnotations
    }
}