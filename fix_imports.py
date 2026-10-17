import os
import re

project_base = "/home/luis/Documents/Projects/Glasfaser/quarkbau-monolith/src/main/java/com/quarkbau/monolith"

mappings = {
    "Project": "project.core",
    "ProjectRepository": "project.core",
    "ProjectService": "project.core",
    "ProjectController": "project.core",
    "ProjectDTO": "project.core",
    "ProjectMapper": "project.core",
    "ProjectStatus": "project.core",
    "ProjectNodesController": "project.core",
    
    "Cluster": "project.cluster",
    "ClusterRepository": "project.cluster",
    "ClusterService": "project.cluster",
    "ClusterController": "project.cluster",
    "ClusterDTO": "project.cluster",
    "ClusterMapper": "project.cluster",
    "Sector": "project.cluster",
    
    "Segment": "segment.core",
    "SegmentRepository": "segment.core",
    "SegmentService": "segment.core",
    "SmartSegmentRecognitionService": "segment.core",
    "SegmentController": "segment.core",
    "SegmentDTO": "segment.core",
    "SegmentMapper": "segment.core",
    "NearestSegmentDTO": "segment.core",
    "GeometryPoint": "segment.core",
    
    "SegmentPermit": "segment.permit",
    "SegmentPermitRepository": "segment.permit",
    "PermitService": "segment.permit",
    "PermitController": "segment.permit",
    
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
    "WorkOrder": "segment.workflow",
    
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
    "PopController": "network.node",
    "NetzverteilerController": "network.node",
    "MuffeController": "network.node",
    "HuepController": "network.node",
    "PopDTO": "network.node",
    "PopMapper": "network.node",
    "NetzverteilerDTO": "network.node",
    "NetzverteilerMapper": "network.node",
    "MuffeDTO": "network.node",
    "MuffeMapper": "network.node",
    "HuepDTO": "network.node",
    "HuepMapper": "network.node",
    "DropConnection": "network.node",
    "DropStatus": "network.node",
    "FiberBox": "network.node",
    
    "Rohrverband": "network.duct",
    "RohrverbandRepository": "network.duct",
    "RohrverbandService": "network.duct",
    "RohrverbandController": "network.duct",
    
    "Employee": "workforce.personnel",
    "InternalEmployee": "workforce.personnel",
    "ExternalEmployee": "workforce.personnel",
    "CompanyRole": "workforce.personnel",
    "EmployeeRepository": "workforce.personnel",
    "EmployeeService": "workforce.personnel",
    "EmployeeController": "workforce.personnel",
    "EmployeeDTO": "workforce.personnel",
    "EmployeeMapper": "workforce.personnel",
    
    "Crew": "workforce.team",
    "CrewRepository": "workforce.team",
    "CrewBusyException": "workforce.team",
    "CrewType": "workforce.team",
    
    "Organization": "workforce.contractor",
    "Subcontractor": "workforce.contractor",
    "OrganizationRepository": "workforce.contractor",
    "SubcontractorRepository": "workforce.contractor",
    "SubcontractorService": "workforce.contractor",
    "SubcontractorController": "workforce.contractor",
    "SubcontractorDTO": "workforce.contractor",
    "SubcontractorMapper": "workforce.contractor",
    "ContractType": "workforce.contractor",
    
    "UtilityLine": "environment.utility",
    "WaterPipe": "environment.utility",
    "GasPipe": "environment.utility",
    "ElectricityCable": "environment.utility",
    "UtilityLineRepository": "environment.utility",
    "UtilityLineService": "environment.utility",
    "UtilityLineController": "environment.utility",
    "UtilityLineDTO": "environment.utility",
    "UtilityLineMapper": "environment.utility",
    "WaterPipeDTO": "environment.utility",
    "GasPipeDTO": "environment.utility",
    "ElectricityCableDTO": "environment.utility",
    
    "Hazard": "environment.safety",
    "FencingPlan": "environment.safety",
    "FencingElement": "environment.safety",
    "HazardRepository": "environment.safety",
    "FencingPlanRepository": "environment.safety",
    "HazardService": "environment.safety",
    "HazardController": "environment.safety",
    "FencingPlanController": "environment.safety",
    "HazardType": "environment.safety",
    "HazardSeverityLevel": "environment.safety",
    "HazardStatus": "environment.safety",
    
    "GlobalExceptionHandler": "shared.exception",
    "InventoryIntegrationService": "shared.integration"
}

def get_package(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
    match = re.search(r'^package\s+([^;]+);', content, re.MULTILINE)
    if match:
        return match.group(1).strip()
    return ""

def process_file(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
        
    original_content = content
    current_package = get_package(filepath)
    
    # Update explicit imports that point to old packages
    for class_name, subdomain in mappings.items():
        new_full_class = f"com.quarkbau.monolith.planning.{subdomain}.{class_name}"
        # Replaces import com.quarkbau.monolith.planning.model.Segment; etc
        content = re.sub(
            r'import\s+com\.quarkbau\.monolith\.planning\.[a-z]+\.' + class_name + r';',
            f'import {new_full_class};',
            content
        )
    
    # Add missing imports for classes used in the file
    imports_to_add = set()
    for class_name, subdomain in mappings.items():
        # Avoid false positives like importing 'Segment' in 'SegmentMapper' where it's part of the name
        # We check for whole words, but also exclude string literals if possible.
        # Simple word boundary check:
        if re.search(r'\b' + class_name + r'\b', content):
            new_full_class = f"com.quarkbau.monolith.planning.{subdomain}.{class_name}"
            new_package = f"com.quarkbau.monolith.planning.{subdomain}"
            
            # Prevent importing itself or if in same package
            if class_name != os.path.splitext(os.path.basename(filepath))[0] and current_package != new_package:
                # Add if not present
                if f"import {new_full_class};" not in content and f"import {new_package}.*;" not in content:
                    imports_to_add.add(f"import {new_full_class};")
                    
    if imports_to_add:
        # inject after package declaration
        if re.search(r'^import\s', content, re.MULTILINE):
            match = re.search(r'^import\s', content, re.MULTILINE)
            idx = match.start()
            imports_str = "\n".join(sorted(list(imports_to_add))) + "\n"
            content = content[:idx] + imports_str + content[idx:]
        else:
            match = re.search(r'^package\s+[^;]+;', content, re.MULTILINE)
            if match:
                idx = match.end()
                imports_str = "\n\n" + "\n".join(sorted(list(imports_to_add))) + "\n"
                content = content[:idx] + imports_str + content[idx:]

    if content != original_content:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(content)
        print(f"Updated imports in {filepath}")

all_files = []
for root, dirs, files in os.walk(project_base):
    for f in files:
        if f.endswith('.java'):
            all_files.append(os.path.join(root, f))

for f in all_files:
    process_file(f)

