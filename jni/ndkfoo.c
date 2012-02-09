#include <jni.h>

JNIEXPORT void JNICALL Java_com_parkour_occular_CamLayer_nativeSobel(
		JNIEnv *env, jobject this,
		jbyteArray frame, jint width, jint height, jintArray out)
		//jbyteArray frame, jint width, jint height, jfloatArray out)
{
	//jint *dest_buf = (jint*) ((*env)->GetDirectBufferAddress(env, out));
	jboolean frame_copy, out_copy;
	//jint *src_buf = (*env)->GetByteArrayElements(env, frame, &frame_copy);
	jbyte *src_buf = (*env)->GetByteArrayElements(env, frame, &frame_copy);
	jint *dest_buf = (*env)->GetIntArrayElements(env, out, &out_copy);


	int bwCounter = 0, crimonacounter = 0;
	int yuvsCounter = 0, x, y, pos = width;
	int sobelX, sobelY, sobelFinal;
	for (y = 1; y < height-1; y++) {
		//pos += width;
		pos = y * width + 1;
		for (x = 1; x < width-1; x++) {
			//pos = y*height + x;
			pos++;
			sobelX = src_buf[pos+x+1] - src_buf[pos+x-1] + src_buf[pos+1] + src_buf[pos+1] - src_buf[pos-1] - src_buf[pos-1] + src_buf[pos-x+1] - src_buf[pos-x-1];
			sobelY = src_buf[pos+x+1] + src_buf[pos+x] + src_buf[pos+x] + src_buf[pos+x-1] - src_buf[pos-x+1] - src_buf[pos-x] - src_buf[pos-x] - src_buf[pos-x-1];
			sobelFinal = (sobelX + sobelY) / 2;
			if(sobelFinal < 48) sobelFinal = 0;
			if(sobelFinal >= 48) sobelFinal = 255;
			dest_buf[pos] = sobelFinal;
			//dest_buf[pos] = (sobelFinal << 0) + (sobelFinal << 8) + (sobelFinal << 16) + (sobelFinal << 24);

		}
	}
	(*env)->ReleaseByteArrayElements(env, frame, src_buf, JNI_ABORT);
	(*env)->ReleaseIntArrayElements(env, out, dest_buf, 0);

}
/*
 * 		jbyteArray frame, jint width, jint height, jfloatArray out)
		//jbyteArray frame, jint width, jint height, jfloatArray out)
{
	//jint *dest_buf = (jint*) ((*env)->GetDirectBufferAddress(env, out));
	jboolean frame_copy, out_copy;
	//jint *src_buf = (*env)->GetByteArrayElements(env, frame, &frame_copy);
	jbyte *src_buf = (*env)->GetByteArrayElements(env, frame, &frame_copy);
	jfloat *dest_buf = (*env)->GetFloatArrayElements(env, out, &out_copy);


	int bwCounter = 0, crimonacounter = 0;
	int yuvsCounter = 0, x, y;
	for (y = 1; y < width-1; y++) {
		for (x = 1; x < height-1; x++) {
			if (src_buf[(y*240 + x) ] < 48) {
				dest_buf[crimonacounter] = (160 - y)*2;
				crimonacounter++;
				dest_buf[crimonacounter] = x*2;
				crimonacounter++;

			} else {
				dest_buf[crimonacounter]     = 0;
				dest_buf[crimonacounter + 1] = 0;
			}
		}
	}
	(*env)->ReleaseByteArrayElements(env, frame, src_buf, JNI_ABORT);
	(*env)->ReleaseFloatArrayElements(env, out, dest_buf, 0);

}
 */

