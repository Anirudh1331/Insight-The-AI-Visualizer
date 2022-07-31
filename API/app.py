from flask import Flask,request
app=Flask(__name__)
import torch, torchvision
import detectron2
from detectron2.utils.logger import setup_logger
setup_logger()
import numpy as np
from flask import jsonify
import urllib.parse
import base64
import os, json, cv2, random, glob, os.path
from os import listdir
import tensorflow as tf
from detectron2 import model_zoo
from detectron2.engine import DefaultPredictor
from detectron2.config import get_cfg
from detectron2.utils.visualizer import Visualizer
from detectron2.data import MetadataCatalog, DatasetCatalog
import urllib.request as ur
import pandas as pd
import tensorflow_hub as hub
import os.path
from numpy import dot
from numpy.linalg import norm
import tensorflow as tf
import base64
import warnings
from waitress import serve
def load_img(path):
    img=path
    img = tf.image.resize_with_pad(img, 224, 224)
    img = tf.image.convert_image_dtype(img,tf.float32)[tf.newaxis, ...]
    return img
def sim_df(item):
    info_df = pd.read_csv('{}d.csv'.format(item))
    feature_vectors = pd.read_csv('{}f.csv'.format(item))
    feature_vectors_df = pd.merge(info_df, feature_vectors, on='id')
    feature_vectors_df.sort_values(by='id', inplace=True)
    feature_vectors_df.reset_index(drop=True, inplace=True)
    feature_vectors_df['feature_vectors'] = feature_vectors_df.feature_vectors.apply(lambda x: x[1:-1].split(', ')) 
    return feature_vectors_df
def cos_sim(x, y):
    return dot(x, y)/(norm(x)*norm(y))
def item_weight(item, num, x=1, y=1, price=100000):
    weight = np.eye(num)
    df = sim_df(item)
    idx = df[df.price >= price].index

    for num in idx:
        weight[num, num] *= x

    for num in range(3200, num+1):
        weight[num, num] *= y
    
    return weight
def recommendation(item,myimages,result_id):
    file_list = myimages
    if len(file_list) == 0:
        return "all"
    else:
        module = hub.load("https://tfhub.dev/google/imagenet/resnet_v2_50/feature_vector/4")
        for i in file_list:
            recommendation2(i,item,module,result_id)
            
def recommendation2(pilImage,item,module,result_id):
    df = sim_df(item)
    cos_sim_df = []
    img = load_img(pilImage)


    features = module(img)
    target_image = np.squeeze(features)

    for idx in df.index:
        vect = [float(num) for num in df.iloc[idx]['feature_vectors']]
        cos_sim_df.append(cos_sim(target_image, vect))
    cos_sim_df = np.array(cos_sim_df)
    num = len(cos_sim_df)

    df['weight'] = cos_sim_df.dot(item_weight(item, num))
    df.sort_values('weight', ascending=False, inplace=True)
    df.reset_index(drop=True, inplace=True)

    for i in range(5):
        result_id.append(df.loc[i].id)
def recommendation3(item,result_id):
    df = sim_df(item)
    for i in range(5):
        result_id.append(df.loc[i].id)
@app.route("/result",methods=["POST","GET"])
def result():
    print("Request asked")
    img_recieved=request.form['image']
    item_type=int(request.form['item'])
    print("Request initiated")
    if img_recieved=="":
        return "all"
    nparr=np.fromstring(base64.b64decode(img_recieved),np.uint8)
    im=cv2.imdecode(nparr,cv2.IMREAD_COLOR)
    myimages=[]
    result_id=[]
    cfg = get_cfg()
    cfg.MODEL.DEVICE = 'cpu'
    cfg.merge_from_file(model_zoo.get_config_file("COCO-InstanceSegmentation/mask_rcnn_R_50_FPN_3x.yaml"))
    cfg.MODEL.ROI_HEADS.SCORE_THRESH_TEST = 0.5
    cfg.MODEL.WEIGHTS = model_zoo.get_checkpoint_url("COCO-InstanceSegmentation/mask_rcnn_R_50_FPN_3x.yaml")
    predictor = DefaultPredictor(cfg)
    outputs = predictor(im)
    v = Visualizer(im[:, :, ::-1], MetadataCatalog.get(cfg.DATASETS.TRAIN[0]), scale=1.2)
    out = v.draw_instance_predictions(outputs["instances"].to("cpu"))
    for cls, xy in zip(outputs["instances"].pred_classes, outputs["instances"].pred_boxes):
        xy = list(map(round, xy.tolist()))
        dst = im[xy[1]:xy[3], xy[0]:xy[2]].copy()
        obj = MetadataCatalog.get(cfg.DATASETS.TRAIN[0]).thing_classes[cls]
        tensor = tf.convert_to_tensor(dst)
        numpydata = tensor
        myimages.append(numpydata)
    recommendation(item_type,myimages,result_id)
    result_id1=set(result_id)
    result_id=list(result_id1)
    aaa=','.join(str(v) for v in result_id)
    print("Request completed")
    return aaa


if __name__=='__main__':
    serve(app,host='0.0.0.0',port=421,threads=8)


