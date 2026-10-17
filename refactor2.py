import os
import re

base_dir = "/home/luis/Documents/Projects/Glasfaser/quarkbau-monolith/src/main/java/com/quarkbau/monolith/planning"

mappings = {
    "ProjectController": "project.core",
    "SegmentController": "segment.core",
    "SubcontractorController": "workforce.contractor",
    "PopController": "network.node",
    "NetzverteilerController": "network.node",
    "MuffeController": "network.node",
    "HuepController": "network.node",
    "HazardController": "environment.safety",
    "RohrverbandController": "network.duct",
    "EmployeeController": "workforce.personnel",
    "ClusterController": "project.cluster",
    "UtilityLineController": "environment.utility",
    "FencingPlanController": "environment.safety",
    "PermitController": "segment.permit",
    "ProjectNodesController": "project.core",
    
    "ProjectDTO": "project.core",
    "ProjectMapper": "project.core",
    "SegmentDTO": "segment.core",
    "SegmentMapper": "segment.core",
    "NearestSegmentDTO": "segment.core",
    "SubcontractorDTO": "workforce.contractor",
    "SubcontractorMapper": "workforce.contractor",
    "PopDTO": "network.node",
    "PopMapper": "network.node",
    "NetzverteilerDTO": "network.node",
    "NetzverteilerMapper": "network.node",
    "MuffeDTO": "network.node",
    "MuffeMapper": "network.node",
    "HuepDTO": "network.node",
    "HuepMapper": "network.node",
    "EmployeeDTO": "workforce.personnel",
    "EmployeeMapper": "workforce.personnel",
    "ClusterDTO": "project.cluster",
    "ClusterMapper": "project.cluster",
    "UtilityLineDTO": "environment.utility",
    "UtilityLineMapper": "environment.utility",
    "WaterPipeDTO": "environment.utility",
    "GasPipeDTO": "environment.utility",
    "ElectricityCableDTO": "environment.utility",
    
    "ContractType": "workforce.contractor",
    "CrewType": "workforce.team",
    "DropConnection": "network.node",
    "DropStatus": "network.node",
    "FiberBox": "network.node",
    "GeometryPoint": "segment.core",
    "ProjectStatus": "project.core",
    "Sector": "project.cluster",
    "WorkOrder": "segment.workflow",
    "HazardType": "environment.safety",
    "HazardSeverityLevel": "environment.safety",
    "HazardStatus": "environment.safety"
}

def get_class_name(filename):
    return os.path.splitext(os.path.basename(filename))[0]

all_files = []
for root, dirs, files in os.walk(base_dir):
    for f in files:
        if f.endswith('.java'):
            all_files.append(os.path.join(root, f))

# Read contents
file_contents = {}
for file_path in all_files:
    with open(file_path, 'r', encoding='utf-8') as f:
        file_contents[file_path] = f.read()

base_pkg = "com.quarkbau.monolith.planning"

for file_path, content in file_contents.items():
    class_name = get_class_name(file_path)
    
    # We update the package of the unmapped ones
    if class_name in mappings:
        new_subdomain = mappings[class_name]
        new_package = f"{base_pkg}.{new_subdomain}"
        content = re.sub(r'package\s+com\.quarkbau\.monolith\.planning\.[a-z]+;', f'package {new_package};', content)
    
    # And we also need to update imports for them in ALL files
    for mapped_class, mapped_subdomain in mappings.items():
        mapped_new_pkg = f"{base_pkg}.{mapped_subdomain}.{mapped_class}"
        content = re.sub(
            r'import\s+com\.quarkbau\.monolith\.planning\.[a-z]+\.' + mapped_class + r';',
            f'import {mapped_new_pkg};',
            content
        )
    
    file_contents[file_path] = content

# Create directories and move files
for file_path, content in file_contents.items():
    class_name = get_class_name(file_path)
    
    if class_name in mappings:
        new_subdomain = mappings[class_name]
        new_dir = os.path.join(base_dir, new_subdomain.replace('.', '/'))
        os.makedirs(new_dir, exist_ok=True)
        
        new_file_path = os.path.join(new_dir, f"{class_name}.java")
        with open(new_file_path, 'w', encoding='utf-8') as f:
            f.write(content)
            
        print(f"Moved {class_name}.java to {new_subdomain}")
        
        # Remove old file if path changed
        if new_file_path != file_path:
            os.remove(file_path)
    else:
        # Just write updated content back (it might have had imports changed)
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(content)

# Cleanup old empty directories
for root, dirs, files in os.walk(base_dir, topdown=False):
    for d in dirs:
        dir_path = os.path.join(root, d)
        try:
            os.rmdir(dir_path)
            print(f"Removed empty directory: {dir_path}")
        except OSError:
            pass # Not empty

