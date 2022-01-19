//THIS IS ALL DEPRECATED, YEETING THIS GOD FORSAKEN MESS OUT OF THE HIGHEST WINDOW FROM THE TALLEST TOWER
// God I hate doing this, but it's the best way for compatibility
// with other mods in a backwards way. Still icky though...
var ASMAPI = Java.type("net.minecraftforge.coremod.api.ASMAPI");
var Opcodes = Java.type("org.objectweb.asm.Opcodes");

var InsnList = Java.type("org.objectweb.asm.tree.InsnList");
var InsnNode = Java.type("org.objectweb.asm.tree.InsnNode");
var VarInsnNode = Java.type("org.objectweb.asm.tree.VarInsnNode");
var TypeInsnNode = Java.type("org.objectweb.asm.tree.TypeInsnNode");
var MethodInsnNode = Java.type("org.objectweb.asm.tree.MethodInsnNode");
var JumpInsnNode = Java.type("org.objectweb.asm.tree.JumpInsnNode")
var FieldInsnNode = Java.type("org.objectweb.asm.tree.FieldInsnNode")
var LabelNode = Java.type("org.objectweb.asm.tree.LabelNode")

var NUM_OF_METHODS = 3;
var MAY_PLACE = ASMAPI.mapMethod("m_5857_");
var MAY_PICKUP = ASMAPI.mapMethod("m_8010_");
var IS_ACTIVE = ASMAPI.mapMethod("m_6659_");

function log(message) {
	print("[Trophy Slots Slot Transformer]: " + message);
}

function patch(method, name, patchFunction) {
	if(method.name != name) {
		return false;
	}

	log("Patching method: " + name + " (" + method.name + ")");
	patchFunction(method.instructions);
	return true;
}

function initializeCoreMod() {
	return {
		"Trophy Slots Slot Transformer": {
			"target": {
				"type": "CLASS",
				"name": "net.minecraft.world.inventory.Slot"
			},
			"transformer": function(classNode) {
				var methods = classNode.methods;
				var count = 0;
				for(var i in methods) {
					if(patch(methods[i], MAY_PLACE, patchBoolCheck)) {
					    count++;
					    continue;
					}
					if(patch(methods[i], MAY_PICKUP, patchBoolCheck)) {
					    count++;
                        continue;
                    }
					if(patch(methods[i], IS_ACTIVE, patchBoolCheck)) {
					    count++;
                        continue;
                    }
                    if (count == NUM_OF_METHODS) {
                        break;
                    }
				}
				return classNode;
			}
		}
	};
}

function patchBoolCheck(instructions) {
    var entryPoint = null;
    //log("Attempting to find entry point");
    for (var i = 0; i < instructions.size(); i++) {
        var instruction = instructions.get(i);
        if (instruction.getOpcode() == Opcodes.ICONST_1) {
            entryPoint = instruction;
            break;
        }
    }

    if (entryPoint == null) {
        //log("Failed to find entry point");
        return;
    }

    //log("Building new instructions");
    var newInstructions = new InsnList();
    var label = new LabelNode();

    newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
    newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/lomeli/trophyslots/core/capabilities/PlayerSlotHelper", "isSlotUnlocked", "(Lnet/minecraft/world/inventory/Slot;)Z", false));
    newInstructions.add(new JumpInsnNode(Opcodes.IFNE, label));
    newInstructions.add(new InsnNode(Opcodes.ICONST_0));
    newInstructions.add(new InsnNode(Opcodes.IRETURN));
    newInstructions.add(label);

    instructions.insertBefore(entryPoint, newInstructions);
}