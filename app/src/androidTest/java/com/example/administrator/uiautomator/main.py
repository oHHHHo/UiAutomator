import os
import json
import subprocess
import time



def parse_json(file_path):
    with open(file_path, 'r', encoding='utf-8') as f:
        js = json.load(f)
        name = js['page_data']['part']
        return name


def find_dir(path):
    # for root, dirs, files in os.walk(path):
    #     print(root)  # 当前目录路径
    #     print(dirs)  # 当前路径下所有子目录
    #     print(files)  # 当前路径下所有非目录子文件
    path = os.path.join(path, 'bilibili')
    dir_list = []
    for item in os.listdir(path):
        if item.startswith('c_'):
            dir_list.append(item)
    return dir_list

def mp4_composer(video, audio, name):
    cmd = "ffmpeg.exe -i " + video + " -i " + audio + " -c copy \"" + name +".mp4\""
    print(cmd)
    subprocess.Popen(cmd)
    time.sleep(3)



if __name__ == '__main__':
    root_path = os.getcwd()
    print(root_path)

    dir_list = find_dir(root_path)
    for i in range(len(dir_list)):
        json_path = os.path.join(root_path, 'bilibili', dir_list[i], 'entry.json')
        vide_name = parse_json(json_path)
        #print(vide_name)

        video_path = os.path.join(root_path, 'bilibili', dir_list[i], '32', 'video.m4s')
        audio_path = os.path.join(root_path, 'bilibili', dir_list[i], '32', 'audio.m4s')

        mp4_composer(video_path, audio_path, vide_name)



