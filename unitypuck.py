#!/usr/bin/env python
import os
import pickle
import sys
from argparse import ArgumentParser
from io import BytesIO
from collections import OrderedDict
from argparse import ArgumentParser

#import yaml
import oyaml as yaml

import unitypack
from unitypack.asset import Asset
from unitypack.export import OBJMesh
from unitypack.utils import extract_audioclip_samples
from unitypack.object import ObjectPointer


class UnityExtract:
    FORMAT_ARGS = {
        "audio": "AudioClip",
        "fonts": "Font",
        "images": "Texture2D",
        "models": "Mesh",
        "shaders": "Shader",
        "text": "TextAsset",
        "video": "MovieTexture",
    }

    def __init__(self, args):
        self.parse_args(args)

    def parse_args(self, args):
        p = ArgumentParser()
        p.add_argument("files", nargs="+")
        p.add_argument("--all", action="store_true",
                       help="Extract all supported types")
        for arg, clsname in self.FORMAT_ARGS.items():
            p.add_argument("--" + arg, action="store_true",
                           help="Extract %s" % (clsname))
        p.add_argument("-a", "--name", nargs="?",
                       default="", help="Picture name")
        p.add_argument("-o", "--outdir", nargs="?",
                       default="", help="Output directory")
        p.add_argument("-f", "--file", nargs="?",
                       default="", help="Output file")
        p.add_argument("--as-asset", action="store_true",
                       help="Force open files as Asset format")
        p.add_argument("--filter", nargs="*",
                       help="Filter extraction for a specific name")
        p.add_argument("-n", "--dry-run", action="store_true",
                       help="Skip writing files")
        self.args = p.parse_args(args)

        self.handle_formats = []
        for a, classname in self.FORMAT_ARGS.items():
            if self.args.all or getattr(self.args, a):
                self.handle_formats.append(classname)

    def run(self):
        for file in self.args.files:
            if self.args.as_asset or file.endswith(".assets"):
                with open(file, "rb") as f:
                    asset = Asset.from_file(f)
                    self.handle_asset(asset)
                continue

            with open(file, "rb") as f:
                bundle = unitypack.load(f)

                for asset in bundle.assets:
                    self.handle_asset(asset)

        return 0

    def get_output_path(self, filename):
        basedir = os.path.abspath(self.args.outdir)
        path = os.path.join(basedir, filename)
        dirs = os.path.dirname(path)
        if not os.path.exists(dirs):
            os.makedirs(dirs)
        return path

    def write_to_file(self, filename, contents, mode="w"):
        path = self.get_output_path(filename)

        if self.args.dry_run:
            print("Would write %i bytes to %r" % (len(contents), path))
            return

        with open(path, mode) as f:
            written = f.write(contents)

        #print("Written %i bytes to %r" % (written, path))
        # exit()

    def handle_asset(self, asset):
        for id, obj in asset.objects.items():
            d = obj.read()
            if obj.type == "tk2dSpriteCollectionData":
                sprites = d["spriteDefinitions"]
                for sprite in sprites:
                    name = sprite["name"]
                    if name == self.args.name:
                        print(yaml.dump(d))
                        # print(d["textures"])
                        # print(d["textures"][0].resolve().type)
                        d = d["textures"][0].resolve()

                        filename = self.args.file
                        #itr = 0
                        #filename = d.name + str(itr) + ".png"
                        #while os.path.isfile(self.get_output_path(filename)):
                        #    itr += 1
                        #    filename = d.name + str(itr) + ".png"

                        try:
                            from PIL import ImageOps
                        except ImportError:
                            print(
                                "WARNING: Pillow not available. Skipping %r." % (filename))
                            continue
                        try:
                            image = d.image
                        except NotImplementedError:
                            print(
                                "WARNING: Texture format not implemented. Skipping %r." % (filename))
                            continue

                        if image is None:
                            print("WARNING: %s is an empty image" % (filename))
                            continue

                        #print("Decoding %r" % (d))
                        # Texture2D objects are flipped
                        img = ImageOps.flip(image)
                        # PIL has no method to write to a string :/
                        output = BytesIO()
                        img.save(output, format="png")
                        self.write_to_file(
                            filename, output.getvalue(), mode="wb")


def asset_representer(dumper, data):
    return dumper.represent_scalar("!asset", data.name)


yaml.add_representer(Asset, asset_representer)


def objectpointer_representer(dumper, data):
    # return yaml.SequenceNode(tag=u'tag:yaml.org,2002:seq', value=[ data.path_id,data.file_id])
    # print(data.resolve())
    return yaml.SequenceNode(tag=u'tag:yaml.org,2002:seq', value=[yaml.ScalarNode(tag=u'tag:yaml.org,2002:str', value=str(data.path_id)), yaml.ScalarNode(tag=u'tag:yaml.org,2002:str', value=str(data.file_id))])


    # return yaml.ScalarNode(tag=u'tag:yaml.org,2002:str', value="[" + str(data.file_id)+","+ str(data.path_id)+"]")
    # return dumper.represent_sequence("!PPtr", [data.file_id, data.path_id])
yaml.add_representer(ObjectPointer, objectpointer_representer)


def unityobj_representer(dumper, data):
    return dumper.represent_mapping("!unitypack:%s" % (data.__class__.__name__), data._obj)


def shader_representer(dumper, data):
    return dumper.represent_mapping("!unitypack:stripped:Shader", {data.name: None})


def textasset_representer(dumper, data):
    return dumper.represent_mapping("!unitypack:stripped:TextAsset", {data.name: None})


def texture2d_representer(dumper, data):
    return dumper.represent_mapping("!unitypack:stripped:Texture2D", {data.name: None})


def mesh_representer(dumper, data):
    return dumper.represent_mapping("!unitypack:stripped:Mesh", {data.name: None})


def movietexture_representer(dumper, data):
    obj = data._obj.copy()
    obj["m_MovieData"] = "<stripped>"
    return dumper.represent_mapping("!unitypack:stripped:MovieTexture", obj)


def main():
    app = UnityExtract(sys.argv[1:])
    exit(app.run())


if __name__ == "__main__":
    main()
