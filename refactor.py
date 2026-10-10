import os
import re
import shutil

base_dir = "/home/luis/Documents/Projects/Glasfaser/quarkbau-monolith/src/main/java/com/quarkbau/monolith/planning"

mappings = {
    "Project": "project.core",
    "ProjectRepository": "project.core",
    "ProjectService": "project.core",
    
    "Cluster": "project.cluster",
    "ClusterRepository": "project.cluster",
    "ClusterService": "project.cluster",
    
    "Segment": "segment.core",
    "SegmentRepository": "segment.core",
    "SegmentService": "segment.core",
    "SmartSegmentRecognitionService": "segment.core",
    
    "SegmentPermit": "segment.permit",
    "SegmentPermitRepository": "segment.permit",
    "PermitService": "segment.permit",
    
    "WorkType": "segment.workflow",
    "WorkflowState": "segment.workflow",
    "ConstructionPhaseState": "segment.workflow",
    "ConstructionPhaseFactory": "segment.workflow",
    "SegmentWorfklowObserver": "segment.workflow",
    "WorkflowStateChangedEvent": "segment.workflow",
    "SurveyState": "segment.workflow",
    "PermittingState": "segment.workflow",
    "ExcavationState": "segment.workflow",
    "DuctInstallationState": "segment.workflow",
    "BackfillState": "segment.workflow",
    "TrenchingState": "segment.workflow",
    "DrillingState": "segment.workflow",
    "RestorationState": "segment.workflow",
    "FiberBlowingState": "segment.workflow",
    "SplicingState": "segment.workflow",
    "AsphaltState": "segment.workflow",
    "QaState": "segment.workflow",
    "DocumentationState": "segment.workflow",
    
    "Pop": "network.node",
    "Netzverteiler": "network.node",
    "Muffe": "network.node",
    "Huep": "network.node",
    "PopRepository": "network.node",
    "NetzverteilerRepository": "network.node",
    "MuffeRepository": "network.node",
    "HuepRepository": "network.node",
    "PopService": "network.node",
    "NetzverteilerService": "network.node",
    "MuffeService": "network.node",
    "HuepService": "network.node",
    
    "Rohrverband": "network.duct",
    "RohrverbandRepository": "network.duct",
    "RohrverbandService": "network.duct",
    
    "Employee": "workforce.personnel",
    "InternalEmployee": "workforce.personnel",
    "ExternalEmployee": "workforce.personnel",
    "CompanyRole": "workforce.personnel",
    "EmployeeRepository": "workforce.personnel",
    "EmployeeService": "workforce.personnel",
    
    "Crew": "workforce.team",
    "CrewRepository": "workforce.team",
    "CrewBusyException": "workforce.team",
    
    "Organization": "workforce.contractor",
    "Subcontractor": "workforce.contractor",
    "OrganizationRepository": "workforce.contractor",
    "SubcontractorRepository": "workforce.contractor",
    "SubcontractorService": "workforce.contractor",
    
    "UtilityLine": "environment.utility",
    "WaterPipe": "environment.utility",
    "GasPipe": "environment.utility",
    "ElectricityCable": "environment.utility",
    "UtilityLineRepository": "environment.utility",
    "UtilityLineService": "environment.utility",
    
    "Hazard": "environment.safety",
    "FencingPlan": "environment.safety",
    "FencingElement": "environment.safety",
    "HazardRepository": "environment.safety",
    "FencingPlanRepository": "environment.safety",
    "HazardService": "environment.safety",
    
    "GlobalExceptionHandler": "shared.exception",
    "InventoryIntegrationService": "shared.integration"
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

# Generate import replacements
# We look for "import com.quarkbau.monolith.planning.model.Class;" and replace with "import com.quarkbau.monolith.planning.new_package.Class;"
old_prefix = "com.quarkbau.monolith.planning."
base_pkg = "com.quarkbau.monolith.planning"

for file_path, content in file_contents.items():
    class_name = get_class_name(file_path)
    if class_name not in mappings:
        print(f"WARNING: Class {class_name} not found in mappings!")
        continue
    
    new_subdomain = mappings[class_name]
    new_package = f"{base_pkg}.{new_subdomain}"
    
    # Update package declaration
    # Regex to find package declaration and replace it
    content = re.sub(r'package\s+com\.quarkbau\.monolith\.planning\.[a-z]+;', f'package {new_package};', content)
    
    # Update imports of other classes that were moved
    for mapped_class, mapped_subdomain in mappings.items():
        mapped_new_pkg = f"{base_pkg}.{mapped_subdomain}.{mapped_class}"
        # Some imports might be com.quarkbau.monolith.planning.model.Segment, etc.
        # So we regex replace any import ending with .ClassName;
        # Like: import com.quarkbau.monolith.planning.*.ClassName;
        content = re.sub(
            r'import\s+com\.quarkbau\.monolith\.planning\.[a-z]+\.' + mapped_class + r';',
            f'import {mapped_new_pkg};',
            content
        )
    
    file_contents[file_path] = content

# Create directories and move files
for file_path, content in file_contents.items():
    class_name = get_class_name(file_path)
    if class_name not in mappings:
        continue
    
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

# Cleanup old empty directories
for root, dirs, files in os.walk(base_dir, topdown=False):
    for d in dirs:
        dir_path = os.path.join(root, d)
        try:
            os.rmdir(dir_path)
            print(f"Removed empty directory: {dir_path}")
        except OSError:
            pass # Not empty

